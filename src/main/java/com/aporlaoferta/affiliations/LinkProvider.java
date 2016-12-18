package com.aporlaoferta.affiliations;

import com.aporlaoferta.model.OfferCompany;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 18/12/2016
 * Time: 11:10
 */
public interface LinkProvider {
    String createLinkAffiliation(OfferCompany offerCompany, String rawLink) throws MalformedURLException, UnsupportedEncodingException;
}
