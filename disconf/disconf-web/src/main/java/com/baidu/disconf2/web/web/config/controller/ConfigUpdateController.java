package com.baidu.disconf2.web.web.config.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.disconf2.web.service.config.service.ConfigMgr;
import com.baidu.disconf2.web.web.config.validator.ConfigValidator;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.vo.JsonObjectBase;

/**
 * 专用于配置更新
 * 
 * @author liaoqiqi
 * @version 2014-6-24
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/config")
public class ConfigUpdateController extends BaseController {

    protected static final Logger LOG = LoggerFactory
            .getLogger(ConfigUpdateController.class);

    @Autowired
    private ConfigMgr configMgr;

    @Autowired
    private ConfigValidator configValidator;

    /**
     * 配置项的更新
     * 
     * @param confListForm
     * @return
     */
    @RequestMapping(value = "/item/{configId}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonObjectBase updateItem(@PathVariable long configId, String value) {

        // 业务校验
        configValidator.validateUpdateItem(configId, value);

        // 配置是否更新
        boolean isUpdate = configValidator.isValueUpdate(configId, value);

        if (isUpdate == true) {

            LOG.info("start to update config: " + configId);

            configMgr.updateItemValue(configId, value);
            return buildSuccess("修改成功");

        } else {
            LOG.info("config :" + configId + " 's value not change, ignore.");
            return buildSuccess("配置值未改变，未修改。");
        }
    }
}