package com.nttdata.myapp.web.rest.vm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/content")
public class FileController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

	@RequestMapping(value = "/helloworld/person.csv")
	public String personAsCSV(HttpServletResponse response) throws IOException {
		response.setContentType("text/plain; charset=utf-8");
		return "";
	}

	// /ftp/info list all GET
	// /ftp/info/{file} -> info about file..size last modified GET
	// /ftp/download/{file} POST
	// /ftp/upload PUT

	@RequestMapping(value = "/ftp/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> listFtpContents(HttpServletRequest request, HttpServletResponse response) {
		List<String> files = new ArrayList<>();
		FTPClient ftpClient = new FTPClient();
		FileOutputStream fos = null;
		String ftpServer = "localhost";
		try {
			ftpClient.connect(ftpServer);
			boolean login = ftpClient.login("ftp", "ftp");
			if (login) {
				ftpClient.printWorkingDirectory();
				FTPFile[] ftpFiles = ftpClient.listFiles();
				for (FTPFile remoteFile : ftpFiles) {
					files.add(remoteFile.getName());
				}
				boolean logout = ftpClient.logout();
				ftpClient.disconnect();

			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
				ftpClient.disconnect();
			} catch (IOException e) {
			}
		}
		return files;
	}

	@RequestMapping(value = "/ftp/upload", method = RequestMethod.POST)
	public String upload(@RequestParam("file") MultipartFile file) {
		if (file != null) {
			FTPClient ftpClient = new FTPClient();
			String ftpServer = "localhost";
			try {
				ftpClient.connect(ftpServer);
				ftpClient.login("ftp", "ftp");
				ftpClient.storeFile(file.getOriginalFilename(), file.getInputStream());
				ftpClient.logout();

			} catch (SocketException soex) {
				soex.printStackTrace();
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}

		}

		return file.getOriginalFilename();

	}

	@RequestMapping(value = "/ftp/download/{fileName:.+}")
	public ResponseEntity<?> getFile(@PathVariable("fileName") String fileName, HttpServletResponse response) {
		Object responseBody = null;
		FTPClient ftpClient = new FTPClient();
		String ftpServer = "localhost";
		
		try {

			ftpClient.connect(ftpServer);
			boolean loggedIn = ftpClient.login("ftp", "ftp");
			if (loggedIn) {
				FTPFileFilter filter = new FTPFileFilter() {
					@Override
					public boolean accept(FTPFile ftpFile) {
						return (ftpFile.isFile() && ftpFile.getName().equals(fileName));
					}
				};

				FTPFile[] result = ftpClient.listFiles("", filter);
				if (result != null && result.length > 0) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ZipOutputStream zos = new ZipOutputStream(baos);
					zos.putNextEntry(new ZipEntry(fileName));
					ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
					ftpClient.retrieveFile(result[0].getName(), baos2);
					zos.write(baos2.toByteArray());
					zos.closeEntry();
					zos.flush();
					responseBody = baos.toByteArray();

				}
				ftpClient.logout();
			}

		} catch (IOException ex) {
			LOGGER.error(" An error occured when creating a zipFile", ex);
			throw new RuntimeException("ERROR.", ex);
		}
		if (responseBody != null) {
			HttpHeaders headers = new HttpHeaders();
			response.setHeader("Content-Disposition", "attachment; filename=download.zip");
			return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}

	}

	// isBornToday, isOlderThan18 (Single rest endpoint )
	// 2. Upload json/xml to server (Name then endpoint /upload)
	// 3. Show all contents of all uploaded files (Name the endpoint /download)
	// 4. Download all file in a zip....as in evry standard download.

}
