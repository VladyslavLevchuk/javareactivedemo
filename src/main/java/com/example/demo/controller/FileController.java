package com.example.demo.controller;

import com.example.demo.model.FileInfo;
import com.example.demo.model.ResponseMessage;
import com.example.demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileStorageService fileStorageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseMessage> uploadFile(@RequestPart("file") Mono<FilePart> filePartMono) {
        return fileStorageService.save(filePartMono).map(
                fileName -> new ResponseMessage("Uploaded the file successfully: " + fileName)
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<FileInfo> getListOfFiles() {
        Stream<FileInfo> fileInfoStream = fileStorageService.loadAll().map(path -> {
            String fileName = path.getFileName().toString();
            String url = UriComponentsBuilder.newInstance().path("/files/{filename}").buildAndExpand(fileName).toUriString();
            return new FileInfo(fileName, url);
        });
        return Flux.fromStream(fileInfoStream);
    }

    @GetMapping(value = "/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<DataBuffer> getFile(@PathVariable String fileName) {
        return fileStorageService.load(fileName);
    }

    @DeleteMapping("/{fileName}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseMessage> deleteFile(@PathVariable String fileName) {
        boolean deleted = fileStorageService.delete(fileName);
        if (deleted) {
            return Mono.just(new ResponseMessage("Deleted the file successfully: " + fileName));
        } else {
            return Mono.just(new ResponseMessage("Deleted the file unsuccessfully: " + fileName));
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseMessage> deleteFiles() {
        boolean deleted = fileStorageService.deleteAll();
        if (deleted) {
            return Mono.just(new ResponseMessage("Deleted the files successfully"));
        } else {
            return Mono.just(new ResponseMessage("Deleted the files unsuccessfully"));
        }
    }
}
