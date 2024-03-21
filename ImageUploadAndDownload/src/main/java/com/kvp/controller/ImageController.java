package com.kvp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kvp.service.FileStorageService;

@RestController
public class ImageController {
	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping("/saveimage")
	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws Exception {
		String res = fileStorageService.uploadImage(file);

		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	@GetMapping("/download/{fileName}")
	public ResponseEntity<?> downloadImage(@PathVariable String fileName) throws Exception {

		byte[] res = fileStorageService.downloadImage(fileName);

		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(res);
	}
}
