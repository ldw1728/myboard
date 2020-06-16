package com.project.pboard.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.project.pboard.model.FileEntity;
import com.project.pboard.repo.FileRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Service
@Data
@AllArgsConstructor
public class FileService {
    private FileRepository fileRepository;
    private List<FileEntity> temp = new ArrayList<>();
    static Logger logger = LoggerFactory.getLogger(FileService.class);

    public Long saveFile(MultipartFile file, Long bid) throws FileUploadException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new FileUploadException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            FileEntity fileEntity = FileEntity.builder().fileName(fileName).filetype(file.getContentType())
                    .data(file.getBytes()).bid(bid).build();

            return fileRepository.save(fileEntity).getId();
        } catch (IOException i) {
            throw new FileUploadException("Could not store file " + fileName + ". Please try again!", i);
        }
    }

    public List<FileEntity> getFiles(Long bid) {
        temp.clear();
        temp = fileRepository.findByBid(bid);

        return temp;
    }

    public String uploadImgFile(MultipartFile file) throws UnsupportedEncodingException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String path = "/pboardupload/" + email + "/";

        String fileName = genSaveFileName(file.getOriginalFilename());

        try {
            File f = new File(path);

            if (!f.exists())
                f.mkdir();

            path = path + fileName;
            FileOutputStream fos = new FileOutputStream(path);

            fos.write(file.getBytes());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return path;

    }

    private String genSaveFileName(String extName) {
		String fileName = "";
		Calendar calendar = Calendar.getInstance();
		fileName += calendar.get(Calendar.YEAR);
		fileName += calendar.get(Calendar.MONTH);
		fileName += calendar.get(Calendar.DATE);
		fileName += calendar.get(Calendar.HOUR);
		fileName += calendar.get(Calendar.MINUTE);
		fileName += calendar.get(Calendar.SECOND);
		fileName += calendar.get(Calendar.MILLISECOND);
		fileName += extName;
		
		return fileName;
	}
}