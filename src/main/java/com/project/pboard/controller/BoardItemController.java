package com.project.pboard.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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

        List<BoardItemDTO.BoardDetailDTO> temp = bis.getBoardItemsOfPage(pageable);
        model.addAttribute("items", temp);
        model.addAttribute("page", bis.getPagingVO());

        UserInfo user = (UserInfo)auth.getPrincipal();
        model.addAttribute("auth",user);
        
        //model.addAttribute("page", bis.getPageData());

        return "boardmain";
    }

    @GetMapping("/newpost")
    public String createNewPost(){
        return "writepage";
    }
    
    @PostMapping("/newpost")
    public String createNewPost(BoardItemDTO.BoardDetailDTO bdd, @RequestParam("uploadFiles") MultipartFile[] files) throws IllegalAccessException,
            FileUploadException {
       
        Long result = bis.saveBoardItemEntity(bdd);
        if(result == -1){
            return "redirect:/login";
        }else{
            if(files.length > 0){
                for(MultipartFile m : files){
                    logger.debug(fileService.saveFile(m, result)+ "");
                }
            }
            return "redirect:/board/main";
        }
    }

    @GetMapping("/{no}")
    public String detailPage(@PathVariable("no") int no, Model model) throws IllegalAccessException {
        try{
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            BoardItemDTO.BoardDetailDTO boardDetailDTO = bis.getBoardDetailDTOs().get(no-1);

            fileService.getFiles(boardDetailDTO.getId());
            model.addAttribute("files", fileService.getTemp());
            
            bis.saveCount(boardDetailDTO);
            model.addAttribute("boardDto", boardDetailDTO);
            model.addAttribute("comments", commentsService.getComments(boardDetailDTO.getId()));
            model.addAttribute("num", no);
            model.addAttribute("username", userInfo.getMemberDto().getName());
            if(boardDetailDTO.getWriter().getEmail().equals(userInfo.getUsername())){
                model.addAttribute("writer", true);
            }

            return "boarddetail";
        }catch(IndexOutOfBoundsException i){
            return "redirect:/board/main";
        }
    }

    @GetMapping("/detail/{no}/download/{index}")
    public @ResponseBody ResponseEntity<Resource> downloadFile(@PathVariable("index") int index){
        FileEntity file = fileService.getTemp().get(index);

        return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(file.getFileType()))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                            .body(new ByteArrayResource(file.getData()));
    }

    @GetMapping("/edit/{no}")
    public String editPage(@PathVariable("no") int no, Model model){
        BoardItemDTO.BoardDetailDTO dto = bis.getBoardDetailDTOs().get(no-1);
        model.addAttribute("boardItemDTO", dto);

        return "boardupdate";
    }

    @PutMapping("/{no}")
    public String updateBoardItem(BoardItemDTO.BoardDetailDTO boardDTO, @RequestParam("uploadFiles") MultipartFile[] files) throws IllegalAccessException,
            FileUploadException {

        Long result = bis.saveBoardItemEntity(boardDTO);
        if(result == -1){
            return "redirect:/login";
        }else{
            if(files.length > 0){
                for(MultipartFile m : files){
                    logger.debug(fileService.saveFile(m, result)+ "");
                }
            }
            return "redirect:/board/main";
        }
    }

    @DeleteMapping("/{no}")
    public String deletePost(@PathVariable("no") int no){
        commentsService.deleteComments(bis.deletePost(no-1));
        return "redirect:/board/main";

    }

    @GetMapping("/search")
    public String searchBoard(@RequestParam(value = "keyword") String keyWord, Model model, Authentication auth){
        List<BoardItemDTO.BoardDetailDTO> boardIitemDTOs = new ArrayList<>();
        
            boardIitemDTOs = bis.searchBoardItems(keyWord);
        
        model.addAttribute("items", boardIitemDTOs);
        UserInfo user = (UserInfo)auth.getPrincipal();
        model.addAttribute("auth",user);
        return "boardmain";
    }

    @GetMapping("/search/page")
    public String getsearchItems(@RequestParam(value = "stat") String stat, Model model, Authentication auth){
        List<BoardItemDTO.BoardDetailDTO> boardIitemDTOs = new ArrayList<>();
        boardIitemDTOs = bis.getSearchItemsOfPage(stat);

        model.addAttribute("items", boardIitemDTOs);
        UserInfo user = (UserInfo)auth.getPrincipal();
        model.addAttribute("auth",user);
        return "boardmain";
    }

    @PostMapping("/newcomment/{no}/{bid}")
    public String saveNewComment(@PathVariable("no")int no, @PathVariable("bid")Long bid, CommentsDTO commentsDTO, HttpServletRequest request){
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentsDTO.setBid(bid);
        commentsDTO.setWriter(userInfo.getMemberDto().getName());
        commentsService.saveComment(commentsDTO);

        return "redirect:/board/"+no;
    }

    @GetMapping("/{no}/comment/delete/{cid}")
    public String deleteCommnet(@PathVariable("no")int no, @PathVariable("cid")Long cid){
        commentsService.deleteCommnet(cid);

        return "redirect:/board/"+no;
    }


}