package com.ecommerce.project.util;

import com.ecommerce.project.exceptions.APIException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FilesUtil {

    private static final String UPLOAD_DIR="images/";

    public static String uploadFile(MultipartFile file){
       Path targetLocation=null;
       String newFileName="";

       if(file != null && !file.isEmpty()){

           String OriginalFileName= Objects.requireNonNull(file.getOriginalFilename());//the place where you wanna upload the image for e.g. a server.
           String extension=getFileExtention(OriginalFileName);//we replace the original file name with a new custom file name
           String baseName= removeFileExtension(OriginalFileName);
           String TimeStamp= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddss.nnnnnnnnn"));
           newFileName=String.format("%s_%s%s", baseName,TimeStamp,extension);
       }
        targetLocation= Paths.get(UPLOAD_DIR).resolve(newFileName);//resolve(), will convert the string path to a Path.

       try{
           Files.createDirectories(targetLocation.getParent());

           Files.copy(file.getInputStream(),targetLocation);

       } catch (IOException e) {
           throw new APIException("There is an issue saving the image.", HttpStatus.BAD_REQUEST);
       }
        return newFileName;
    }

    public static String RemoveFile(String FileName){
        if(!StringUtils.isBlank(FileName)){
            Path FilePath=Paths.get(UPLOAD_DIR).resolve(FileName);
            try{
               if(Files.exists(FilePath)){
                   Files.delete(FilePath);
               }

            }catch (IOException ignored){

            }
        }
        return "File removed";
    }

    private static String getFileExtention(String fileName){
        int dotIndex= fileName.lastIndexOf('.');
        return (dotIndex != -1) ? fileName.substring(dotIndex) : "";
    }


    private static String removeFileExtension(String fileName){
        int dotIndex= fileName.lastIndexOf('.');
        return (dotIndex != -1) ? fileName.substring(0,dotIndex) : fileName;
    }
}
