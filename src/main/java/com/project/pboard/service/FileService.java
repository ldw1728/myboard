package com.project.pboard.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.project.pboard.model.FileEntity;
import com.project.pboard.repo.FileRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Service
@Data
@AllArgsConstructor
public class FileService {
    static Logger logger = LoggerFactory.getLogger(FileService.class);
    private FileRepository fileRepository;
    private List<FileEntity> temp = new ArrayList<>();

    public Long saveFile(MultipartFile file, Long bid) throws FileUploadException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try{
            if(fileName.contains("..")){
                throw new FileUploadException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            FileEntity fileEntity = FileEntity.builder()
            .fileName(fileName)
            .filetype(file.getContentType())
            .data(file.getBytes())
            .bid(bid)
            .build();

            return fileRepository.save(fileEntity).getId();
        }catch(IOException i){
            throw new FileUploadException("Could not store file " + fileName + ". Please try again!", i);
        }
    }

    public List<FileEntity> getFiles(Long bid){
        temp.clear();
        temp = fileRepository.findByBid(bid);
        logger.debug(temp.size()+"");
        return temp;
    }
}