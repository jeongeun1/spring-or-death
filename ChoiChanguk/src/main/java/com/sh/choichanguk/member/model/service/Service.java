package com.sh.choichanguk.member.model.service;
import com.sh.choichanguk.member.model.dao.MemberRegistMapper;
import com.sh.choichanguk.member.model.dto.FileDto;
import com.sh.choichanguk.member.model.dto.MemberRegistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {
    private final MemberRegistMapper memberRegistMapper;

    public String findByEmail(String userEmail) {
        return memberRegistMapper.findByEmail(userEmail);
    }

    public int insertMember(MemberRegistDto memberRegistDto) {
        return memberRegistMapper.insertMember(memberRegistDto);
    }

//    파일 업로드에 관한 메소드
@Value("${ftp.server.host}")
private String server;

    @Value("${ftp.server.port}")
    private int port;

    @Value("${ftp.server.username}")
    private String username;

    @Value("${ftp.server.password}")
    private String password;

    public FileDto upload(MultipartFile multipartFile) throws IOException {
        FTPClient ftpClient = new FTPClient(); // dependencies 해줘야 함
        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            try (InputStream inputStream = multipartFile.getInputStream()) {
                String contentType = multipartFile.getContentType();
                String originalFilename = multipartFile.getOriginalFilename();
                String renamedFilename = getRenamedFilename(originalFilename);
                boolean done = ftpClient.storeFile(renamedFilename, inputStream);
                if (!done)
                    throw new RuntimeException("[" + multipartFile + "] 파일 업로드에 실패했습니다.");

                // Builder패턴을 사용한 객체 생성
                return FileDto.builder()
                        .originalFilename(originalFilename)
                        .renamedFilename(renamedFilename)
                        .contentType(contentType)
                        .build();
            }
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getRenamedFilename(String originalFilename) {
        return "%s_%s".formatted(UUID.randomUUID().toString(), originalFilename);
    }
}
