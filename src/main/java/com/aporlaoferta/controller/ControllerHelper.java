package com.aporlaoferta.controller;

import com.aporlaoferta.model.TheResponse;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 03/02/2015
 * Time: 08:27
 */
public class ControllerHelper {

    public static void addEmptyDatabaseObjectMessage(TheResponse result, Logger LOG) {
        String resultDescription = String.format(ResultCode.DATABASE_RETURNED_EMPTY_OBJECT.getResultDescription());
        result.assignResultCode(ResultCode.DATABASE_RETURNED_EMPTY_OBJECT);
        LOG.error(resultDescription);
    }

    public static void addEmptyDatabaseObjectMessage(TheResponse result, String addon, Logger LOG) {
        String resultDescription = String.format(ResultCode.DATABASE_RETURNED_EMPTY_OBJECT.getResultDescription() + addon);
        result.assignResultCode(ResultCode.DATABASE_RETURNED_EMPTY_OBJECT);
        result.setDescription(resultDescription);
        LOG.error(resultDescription);
    }
}
