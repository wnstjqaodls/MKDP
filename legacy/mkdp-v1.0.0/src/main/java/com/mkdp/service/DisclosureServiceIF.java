package com.mkdp.service;

import java.util.List;

public interface DisclosureServiceIF {

    // List<Integer> getUniqueNumber();
    void updateUniqueNumber (List<String> companies);
    void updateCompanies(List<String>companies);
    List<String> parseCorpCodeXml(byte[] zipFile);

}