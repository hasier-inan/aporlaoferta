package com.aporlaoferta.controller;

import com.aporlaoferta.model.OfferCompany;
import com.aporlaoferta.model.TheResponse;
import com.aporlaoferta.model.validators.ValidationException;
import com.aporlaoferta.service.CompanyManager;
import com.aporlaoferta.utils.OfferValidatorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasiermetal on 2/02/14.
 */
@Controller
public class CompanyController {

    private final Logger LOG = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private OfferValidatorHelper offerValidatorHelper;

    @Autowired
    private CompanyManager companyManager;

    @RequestMapping(value = "/createCompany", method = RequestMethod.POST)
    @ResponseBody
    public TheResponse createCompany(@RequestBody OfferCompany offerCompany) {
        TheResponse result = new TheResponse();
        try {
            this.offerValidatorHelper.validateCompany(offerCompany);
            OfferCompany company = this.companyManager.createCompany(offerCompany);
            if (company == null) {
                ControllerHelper.addEmptyDatabaseObjectMessage(result, LOG);
            } else {
                String okMessage = String.format("Company successfully created. Id: %s", company.getId());
                LOG.info(okMessage);
                result.assignResultCode(ResultCode.ALL_OK, okMessage);
            }
        } catch (ValidationException e) {
            String resultDescription = ResultCode.CREATE_COMPANY_VALIDATION_ERROR.getResultDescription();
            LOG.warn(resultDescription, e);
            result.assignResultCode(ResultCode.CREATE_COMPANY_VALIDATION_ERROR);
        }
        return result;
    }

    @RequestMapping(value = "/companyList", method = RequestMethod.POST)
    @ResponseBody
    public List<OfferCompany> getCompanies() {
        List<OfferCompany> actualCompanies = this.companyManager.getAllCompanies();
        if (actualCompanies == null) {
            return new ArrayList<>();
        }
        return actualCompanies;
    }


}
