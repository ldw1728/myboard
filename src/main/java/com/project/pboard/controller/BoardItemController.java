package com.project.pboard.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.project.pboard.UserInfo;
import com.project.pboard.model.BoardItemDTO;
import com.project.pboard.model.CommentsDTO;
import com.project.pboard.model.FileEntity;
import com.project.pboard.service.BoardItemService;
import com.project.pboard.service.CommentsService;
import com.project.pboard.service.FileService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping(value="/board")
public class BoardItemController {

    static Logger logger = LoggerFactory.getLogger(BoardItemController.class);
    private BoardItemService bis;
    private FileService fileService;
    private CommentsService commentsService;
    
    @GetMapping("/main")
    public String getAllList(Model model, @PageableDefault Pageable pageable, Authentication auth){
        //게시판 메인
        List<BoardItemDTO.BoardDetailDTO> temp = bis.getBoardItemsOfPage(pageable);
        model.addAttribute("items", temp); 
        model.addAttribute("page", bis.getPagingVO());

        UserInfo user = (UserInfo)auth.getPrincipal();
        model.addAttribute("auth",user);

        return "boardmain";
    }

    @GetMapping("/newpost")
    public String createNewPost(){
        //게시물 작성 페이지
        return "writepage";
    }
    
    @PostMapping("/newpost")
    public String createNewPost(BoardItemDTO.BoardDetailDTO bdd, @RequestParam("uploadFiles") MultipartFile[] files) throws IllegalAccessException,
            FileUploadException {

            return saveBoardItem(bdd, files);
    }

    @GetMapping("/{no}")
    public String detailPage(@PathVariable("no") int no, Model model) throws IllegalAccessException {
        //게시물의 세부내용을 출력하는 페이지리턴
        try{
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //유저불러오기
            BoardItemDTO.BoardDetailDTO boardDetailDTO = bis.getBoardDetailDTOs().get(no-1); 

            fileService.getFiles(boardDetailDTO.getId()); //게시물과 관련된 첨부파일 불러오기
            model.addAttribute("files", fileService.getTemp());
            
            bis.saveCount(boardDetailDTO); //게시물 카운터
        
            model.addAttribute("boardDto", boardDetailDTO); //게시물내용
            model.addAttribute("comments", commentsService.getComments(boardDetailDTO.getId())); //댓글 
            model.addAttribute("num", no); //현재페이지 no
            model.addAttribute("username", userInfo.getMemberDto().getName());//유저name
            
            if(boardDetailDTO.getWriter().getEmail().equals(userInfo.getUsername())){ //해당 게시물의 작성자인지 판별(삭제, 추가 기능을 위해)
                model.addAttribute("writer", true);
            }
            return "boarddetail";

        }catch(IndexOutOfBoundsException i){
            return "redirect:/board/main";
        }
    }

    @GetMapping("/detail/{no}/download/{index}")
    public @ResponseBody ResponseEntity<Resource> downloadFile(@PathVariable("index") int index){
        //첨부파일 다운로드을 구현
        FileEntity file = fileService.getTemp().get(index);

        return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(file.getFileType()))//컨텐트의 type을 각 파일의 type으로 명시
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                            .body(new ByteArrayResource(file.getData())); //body에 byteData를 담아 response.
    }

    @GetMapping("/edit/{no}")
    public String editPage(@PathVariable("no") int no, Model model){
        //수정 페이지.
        BoardItemDTO.BoardDetailDTO dto = bis.getBoardDetailDTOs().get(no-1);
        model.addAttribute("boardItemDTO", dto);

        return "boardupdate";
    }

    @PutMapping("/{no}")
    public String updateBoardItem(BoardItemDTO.BoardDetailDTO boardDTO, @RequestParam("uploadFiles") MultipartFile[] files) throws IllegalAccessException,
            FileUploadException {
               return saveBoardItem(boardDTO, files);
        
    }

    @DeleteMapping("/{no}")
    public String deletePost(@PathVariable("no") int no){
        //삭제
        commentsService.deleteComments(bis.deletePost(no-1));
        return "redirect:/board/main";
    }

    @GetMapping("/search")
    public String searchBoard(@RequestParam(value = "keyword") String keyWord, Model model, Authentication auth){
        //검색기능
        List<BoardItemDTO.BoardDetailDTO> boardIitemDTOs = new ArrayList<>();
        
        boardIitemDTOs = bis.searchBoardItems(keyWord);
        model.addAttribute("items", boardIitemDTOs);

        UserInfo user = (UserInfo)auth.getPrincipal();
        model.addAttribute("auth",user);
        return "boardmain";
    }

    @GetMapping("/search/page")
    public String getsearchItems(@RequestParam(value = "stat") String stat, Model model, Authentication auth){
        //검색모드에 대한 페이징.
        List<BoardItemDTO.BoardDetailDTO> boardIitemDTOs = new ArrayList<>();
        boardIitemDTOs = bis.getSearchItemsOfPage(stat); //stat은 다음페이지, 이전페이지를 구분

        model.addAttribute("items", boardIitemDTOs);
        UserInfo user = (UserInfo)auth.getPrincipal();
        model.addAttribute("auth",user);
        return "boardmain";
    }

    @PostMapping("/newcomment/{no}/{bid}")
    public String saveNewComment(@PathVariable("no")int no, @PathVariable("bid")Long bid, CommentsDTO commentsDTO, HttpServletRequest request){
        //댓글 등록 기능
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentsDTO.setBid(bid);
        commentsDTO.setWriter(userInfo.getMemberDto().getName());
        commentsService.saveComment(commentsDTO);

        return "redirect:/board/"+no;
    }

    @GetMapping("/{no}/comment/delete/{cid}")
    public String deleteCommnet(@PathVariable("no")int no, @PathVariable("cid")Long cid){
        //댓글 삭제기능.
        commentsService.deleteCommnet(cid);

        return "redirect:/board/"+no;
    }   

    @PostMapping("/post/upload/image")
    public @ResponseBody ResponseEntity<String> uploadImage(MultipartHttpServletRequest request)
            throws IOException {
                //게시물 작성 시 사진을 업로드하기위해 에디터에 추가 하면 실행된다.
                //이미지를 서버 로컬 경로에 저장하는 작업.

        MultipartFile file= request.getFile("upload"); 
        String path = fileService.uploadImgFile(file); 
                
        return new ResponseEntity<>(path, HttpStatus.OK); //에디터로 경로를 리턴, 이 경로로 img태그의 src의 값을 설정하게 된다.
    }

    @GetMapping(value="/pboardupload/{username}/{pic}", produces ={MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE} )
    public @ResponseBody void responseImg(@PathVariable("username")String username, @PathVariable("pic")String pic, HttpServletResponse response)
            throws IOException { 
                //사진이 업로드된 게시물을 클릭 시 실행된다.
                //이 메소드의 url값은 html상 img태그안의 src의 값과 동일
        ServletOutputStream out =  response.getOutputStream(); //stream을 이용하여 reponse하기위해.
        FileInputStream in = new FileInputStream("/pboardupload/"+username+"/"+pic); //로컬이미지를 받아옴       
                
        FileCopyUtils.copy(in,out); //복사
        if(in != null){
            in.close();
        }
        out.flush();
    }


    public String saveBoardItem(BoardItemDTO.BoardDetailDTO bdd, MultipartFile[] files) 
    throws IllegalAccessException, FileUploadException {
        Long result = bis.saveBoardItemEntity(bdd);
        if(result == -1){ //user와 연결이 끊기면 로그인 페이지로 이동.
            return "redirect:/login";
        }else{
            if(files.length > 0 && files[0].getSize() != 0){
                for(MultipartFile m : files){
                    logger.debug(fileService.saveFile(m, result)+ "");
                } 
            }
            return "redirect:/board/main";
        }
    }
}