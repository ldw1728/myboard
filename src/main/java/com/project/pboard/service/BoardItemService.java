package com.project.pboard.service;

import java.util.ArrayList;
import java.util.List;

import com.project.pboard.UserInfo;
import com.project.pboard.model.BoardItemDTO;
import com.project.pboard.model.BoardItemEntity;
import com.project.pboard.model.PagingVO;
import com.project.pboard.repo.BoardItemRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@Service
@RequiredArgsConstructor
public class BoardItemService {

    private  List<BoardItemDTO.BoardDetailDTO> boardDetailDTOs = new ArrayList<>();
    private PagingVO pagingVO;
    private int searchPage = 0;

    @NonNull
    private BoardItemRepository bir;

    public List<BoardItemDTO.BoardDetailDTO> getBoardItemsOfPage(Pageable pageable){
        
        pagingVO = new PagingVO();
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber()-1);
        pageable = PageRequest.of(page, 10, Sort.by("id").descending()); 
        

        Page<BoardItemEntity> pageData = bir.findAll(pageable);

        setPage(page, pageData.getTotalPages(), pageData.getNumber());
        
        ModelMapper mm = new ModelMapper();
        boardDetailDTOs = mm.map(pageData.getContent(), new TypeToken<List<BoardItemDTO.BoardDetailDTO>>(){}.getType());
        //Modelmapper를 이용하여 entity -> DTO 변환
       
        return boardDetailDTOs; //boardMainDTO를 이용하여 해당페이지에 필요한 내용만 컨트롤러로 넘겨준다.
    }

   

    @Transactional
    public Long saveBoardItemEntity(BoardItemDTO.BoardDetailDTO bidto) throws IllegalAccessException {
        try{
            UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(userInfo.isEnabled()){
                bidto.setWriter(userInfo.getMemberDto());
                return bir.save(bidto.toEntity()).getId();
            }else{
                 return (long) -1;
            }
           
        }catch(NullPointerException n){
            return (long) -1;
        }
    }

    public Long saveCount(BoardItemDTO.BoardDetailDTO bidto){
        bidto.setCount(bidto.getCount()+1);
        return bir.save(bidto.toEntity()).getId();
    }
    @Transactional
	public Long deletePost(int id) {
        Long bid = boardDetailDTOs.get(id).getId();
        bir.deleteById(bid);

        return bid;
    }

    @Transactional
    public List<BoardItemDTO.BoardDetailDTO> searchBoardItems(String keyWord){
        List<BoardItemEntity> boardEntity = bir.findByTitleContaining(keyWord);

        if(boardEntity.isEmpty()){
            boardDetailDTOs.clear();
            return boardDetailDTOs;
        }

        searchPage = 0;
        pagingVO = null;

        ModelMapper mm = new ModelMapper();
        boardDetailDTOs = mm.map(boardEntity, new TypeToken<List<BoardItemDTO.BoardDetailDTO>>(){}.getType());
        return boardDetailDTOs;
        
    }

    public List<BoardItemDTO.BoardDetailDTO> getSearchItemsOfPage(String stat){

        int element = 10;
        int totalPages = (boardDetailDTOs.size()%element > 0) ? (boardDetailDTOs.size()/element)+1 : (boardDetailDTOs.size()/element);

        if(stat.equals("next")){
            if(searchPage < totalPages-1)
                        searchPage++;
        }
           
        else if(stat.equals("prev"))
                if(searchPage > 0)
                        searchPage--;

         List<BoardItemDTO.BoardDetailDTO> temp = new ArrayList<>();
        int sop = searchPage * element; //페이지의 시작 게시물
        if((sop) <= boardDetailDTOs.size() ){
            for(int i = sop; i<sop+element; i++){
                if(boardDetailDTOs.size() <= i){
                   break;
                }
                temp.add(boardDetailDTOs.get(i));
            }
            return temp;
        }else{
            return temp;
        }
    }

    public void setPage(int page, int totalPages, int number){
        if(page%5 == 0){
            pagingVO.setFP(page+1);
        }else if(page%5 == 4){
            pagingVO.setFP(page-3);
        }
        if(number+1 == totalPages)
          pagingVO.setPaging(totalPages, number);
        else
        pagingVO.setPaging(totalPages, number+1);
    }
}