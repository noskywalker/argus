package com.monitor.argus.mis.controller.system;

import com.monitor.argus.bean.system.ModuleBean;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.service.system.IModuleRedisService;
import com.monitor.argus.service.system.IModuleService;
import com.monitor.argus.service.system.IUserRedisService;
import com.monitor.argus.common.model.DataGrid;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.common.util.ConstantsForRedis;
import com.monitor.argus.mis.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Description: 模块
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午08:54:00
 * @Version: V1.0
 */
@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ModuleController.class);

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private IModuleRedisService moduleRedisService;

    @Autowired
    private IUserRedisService userRedisService;

    /**
     * @return
     * @throws Exception
     * @Description:
     * @Author: alex zhang
     * @CreateDate: 2015-3-19 下午06:48:05
     * @Version: V1.0
     */
    @RequestMapping("/getRoleModules")
    @ResponseBody
    public ResponseData getRoleModules(HttpServletRequest request) throws Exception {
        UserBean userBean = userRedisService.getUserBeanSession(request);
        ResponseData json = new ResponseData();
        if (userBean == null) {
            json.setSuccess(false);
            json.setMsg("亲爱的用户：您长时间没有操作，为了帐号安全请重新登录！");
            return json;
        }
        logger.info("获取菜单模块{}Start", userBean);
        if (userBean != null) {
            String roleId = userBean.getId();
            String redisKey = ConstantsForRedis.MGR_ROLE_MODULE_PRE + roleId;
            List<ModuleBean> moduleModules = moduleRedisService.getModuleBeanList(redisKey);
            if (!(moduleModules != null && moduleModules.size() > 0)) {
                moduleModules = moduleService.getRoleModules(roleId);
                moduleRedisService.addModuleBeanList(redisKey, moduleModules);
            }
            json.setSuccess(true);
            json.setObj(moduleModules);
        }
        return json;
    }

    /**
     * @return
     * @throws Exception
     * @Description:
     * @Author: alex zhang
     * @CreateDate: 2015-3-19 下午06:48:05
     * @Version: V1.0
     */
    @RequestMapping("/getModulesTreeByRole")
    @ResponseBody
    public List<ModuleBean> getModulesTreeByRole(@RequestParam(value = "roleId") String roleIdForSet,
                                                 HttpServletRequest request) throws Exception {
        UserBean userBean = userRedisService.getUserBeanSession(request);
        logger.info("获取菜单模块\n\t@by : " + userBean.getId() + "(" + userBean.getUserName() + ")Start");
        if (!(roleIdForSet != null && !roleIdForSet.isEmpty())) {
            return null;
        }
        List<ModuleBean> moduleModules = moduleService.getRoleModulesForSet(roleIdForSet);

        if (moduleModules != null && moduleModules.size() > 0) {
            for (ModuleBean moduleBean : moduleModules) {
                if (moduleBean.getSubModules() != null && moduleBean.getSubModules().size() > 0) {
                    // 为TreeGrid组织数据
                    moduleBean.setChildren(moduleBean.getSubModules());
                    moduleBean.setSubModules(null);
                }
            }
        }
        return moduleModules;
    }

    /**
     * 菜单列表DataGrid
     *
     * @param moduleBean
     * @param session
     * @return DataGrid
     * @date 2014-5-16
     * @author alex zhang
     */
    @RequestMapping("/dataGrid")
    @ResponseBody
    public DataGrid dataGrid(ModuleBean moduleBean, HttpSession session) {
        logger.info("调用dataGrid方法展示module页面");
        return moduleService.moduleBeanDataGrid(moduleBean);
    }

    /**
     * 菜单管理页面
     *
     * @param request
     * @return
     * @Author null
     * @Date 2014-5-22 下午05:07:46
     * @Version V1.0
     */
    @RequestMapping("/moduleManager")
    public String moduleManager(HttpServletRequest request) {
        return "/system/moduleManager";
    }

    /**
     * 宜定盈渠道债权页面DateGrid
     *
     * @param moduleBean
     * @param session
     * @return
     * @Author null
     * @Date 2014-5-20
     * @Version V1.0
     */
    @RequestMapping("/moduleDataGrid")
    @ResponseBody
    public DataGrid moduleDataGrid(ModuleBean moduleBean, HttpSession session) {
        logger.info("调用dataGrid方法展示p2pservice页面");
        return moduleService.moduleBeanDataGrid(moduleBean);
    }

    /**
     * 渠道配额添加页面
     *
     * @param request
     * @return
     * @Author null
     * @Date 2014-5-20 下午03:41:51
     * @Version V1.0
     */
    @RequestMapping("/moduleAddPage")
    public String moduleAddPage(HttpServletRequest request) {
        // ModuleBean moduleBean = new ModuleBean();
        // request.setAttribute("moduleBean", moduleBean);
        //
        // // 返回可分配的宜定盈列表
        // P2pserviceChannelBean p2pserviceChannelBean = new
        // P2pserviceChannelBean();
        // List<P2pserviceChannelBean> p2pserviceChannelBeanList = moduleService
        // .getP2pserviceChannelBeanList(p2pserviceChannelBean);
        // request.setAttribute("p2pserviceChannelBeanList",
        // p2pserviceChannelBeanList);
        return "/system/moduleAdd";
    }

    /**
     * 渠道配额添加页面
     *
     * @param request
     * @return
     * @Author null
     * @Date 2014-5-20 下午03:41:51
     * @Version V1.0
     */
    @RequestMapping("/getRootModuleList")
    @ResponseBody
    public List<ModuleBean> getRootModuleList(HttpServletRequest request) {
        UserBean operator = userRedisService.getUserBeanSession(request);
        logger.info("add Or Update Module 保存宜定盈app配额信息\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ")Start");
        ModuleBean moduleBean = new ModuleBean();
        moduleBean.setStatus(1);
        moduleBean.setParentId(0);
        List<ModuleBean> moduleList = moduleService.getModuleBeanList(moduleBean);
        return moduleList;
    }

    /**
     * 添加Or修改渠道配额
     *
     * @param moduleBean
     * @return
     * @Author null
     * @Date 2014-5-20 下午03:39:54
     * @Version V1.0
     */
    @RequestMapping(value = "/addOrUpdateModule", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResponseData addOrUpdateModule(ModuleBean moduleBean, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSession(request);
        logger.info("add Or Update Module 保存宜定盈app配额信息\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ")Start");
        ResponseData jsonResponse = new ResponseData();
        boolean boo = false;
        if (moduleBean == null) {
            // 返回错误
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("信息不完整");
            return jsonResponse;
        }
        if (moduleBean.getId() != null) {
            // 修改操作
            moduleBean.setOperatorId(operator.getId());
            boo = moduleService.updateModuleBean(moduleBean);
        } else {
            // 添加
            moduleBean.setCreatorId(operator.getId());
            moduleBean.setStatus(1);
            if (moduleBean.getParentId() == null) {
                moduleBean.setParentId(0);
            }
            boo = moduleService.addModuleBean(moduleBean);
        }

        if (boo) {
            moduleRedisService.deleteModuleBean(ConstantsForRedis.MGR_ROLE_MODULE_PRE);
            jsonResponse.setSuccess(true);
            jsonResponse.setMsg("保存成功！");
            return jsonResponse;
        } else {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("保存失败！");
            return jsonResponse;
        }
    }

    /**
     * 删除渠道配额
     *
     * @param moduleBean
     * @return
     * @Author null
     * @Date 2014-5-20 下午03:39:54
     * @Version V1.0
     */
    @RequestMapping(value = "/deleteModule", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResponseData deleteModule(ModuleBean moduleBean, HttpServletRequest request) throws Exception {
        UserBean operator = userRedisService.getUserBeanSession(request);
        logger.info("delete Module 删除宜定盈app配额信息\n\t@by : " + operator.getId() + "(" + operator.getUserName() + ")Start");

        ResponseData jsonResponse = new ResponseData();
        boolean boo = false;

        moduleBean.setOperatorId(operator.getId());
        moduleBean.setStatus(0);
        boo = moduleService.updateModuleBean(moduleBean);

        if (boo) {
            moduleRedisService.deleteModuleBean(ConstantsForRedis.MGR_ROLE_MODULE_PRE);
            jsonResponse.setSuccess(true);
            jsonResponse.setMsg("删除成功！");
            return jsonResponse;
        } else {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("删除失败！");
            return jsonResponse;
        }
    }

}
