package com.mkdp.service;

import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Service
public class DisclosureService implements DisclosureServiceIF {
    @Autowired
    private SqlSessionFactory ssf;

    @Override
    public void updateUniqueNumber(List<String> companies) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUniqueNumber'");
    }
    @Override 
    public List<String> parseCorpCodeXml(byte[] zipFile) {
        List<String> result = new ArrayList<>();
        try {
            // ZIP 파일 압축해제
            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipFile));
            ZipEntry entry;
            
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".xml")) {
                    // XML 파싱
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(zis);
                    
                    NodeList corpList = doc.getElementsByTagName("corp");
                    for (int i = 0; i < corpList.getLength(); i++) {
                        Element corp = (Element) corpList.item(i);
                        String corpCode = corp.getElementsByTagName("corp_code").item(0).getTextContent();
                        result.add(corpCode);
                    }
                }
                zis.closeEntry();
            }
            zis.close();
        } catch (Exception e) {
            throw new RuntimeException("XML 파싱 중 오류 발생", e);
        }
        return result;
    }
    
    @Override
    public void updateCompanies(List<String> companies) {
        ssf.openSession().getMapper(DisclosureService.class).updateCompanies(companies);
    }
}
