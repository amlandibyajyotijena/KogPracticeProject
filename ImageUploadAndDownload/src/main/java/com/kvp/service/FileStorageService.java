package com.kvp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kvp.entity.ImageData;
import com.kvp.repo.ImageRepo;
import com.kvp.utility.ImageUtil;

@Service
public class FileStorageService {
	@Autowired
	private ImageRepo imageRepo;

	public String uploadImage(MultipartFile file) throws Exception {
		ImageData result = imageRepo.save(ImageData.builder().name(file.getOriginalFilename())
				.type(file.getContentType()).imageData(ImageUtil.compress(file.getBytes())).build());
		if (result != null) {
			return "image saved with name:" + file.getOriginalFilename();
		} else {
			return "image not saved";
		}
	}

	public byte[] downloadImage(String fileName) throws Exception {
		Optional<ImageData> img = imageRepo.findByName(fileName);
		return ImageUtil.decompress(img.get().getImageData());

	}
}
