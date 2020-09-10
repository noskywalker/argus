package com.monitor.argus.mis.controller.system;

import com.monitor.argus.bean.system.RoleBean;
import com.monitor.argus.bean.system.RoleModuleBean;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.common.model.DataGrid;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.common.util.ConstantsForRedis;
import com.monitor.argus.dao.redis.IRedisKeyBaseDao;
import com.monitor.argus.service.system.IRoleModuleService;
import com.monitor.argus.service.system.IRoleService;
import com.monitor.argus.service.system.IUserRedisService;
import com.monitor.argus.mis.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Description: 角色
 *
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午08:54:00
 * @Version: V1.0
 *
 *
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRoleModuleService roleModuleService;
    @Autowired
    private IUserRedisService userRedisService;
    @Autowired
    IRedisKeyBaseDao redisKeyBaseDao;

    /**
     * 角色管理页面
     *
     * @param request
     * @return
     * @Author null
     * @Date 2014-5-22 下午05:07:46
     * @Version V1.0
     *
     */
    @RequestMapping("/roleManager")
    public String roleManager(HttpServletRequest request) {
        return "/system/roleManager";
    }

    /**
     * 角色列表DataGrid
     *
     * @param role
     * @param session
     * @return
     * @return DataGrid
     * @date 2014-5-16
     * @author alex zhang
     */
    @RequestMapping("/dataGrid")
    @ResponseBody
    public DataGrid dataGrid(RoleBean roleBean, HttpSession session) {
        logger.info("调用dataGrid方法展示role页面");
        return roleService.roleBeanDataGrid(roleBean);
    }

    /**
     * 宜定盈渠道债权页面DateGrid
     *
     * @param roleBean
     * @param session
     * @return
     * @Author null
     * @Date 2014-5-20
     * @Version V1.0
     *
     */
    @RequestMapping("/roleDataGrid")
    @ResponseBody
    public DataGrid<RoleBean> roleDataGrid(RoleBean roleBean, HttpSession session) {
        logger.info("调用dataGrid方法展示p2pservice页面");
        return roleService.roleBeanDataGrid(roleBean);
    }

    /**
     * 渠道配额添加页面
     *
     * @param request
     * @return
     * @Author null
     * @Date 2014-5-20 下午03:41:51
     * @Version V1.0
     *
     */
    @RequestMapping("/roleAddPage")
    public String roleAddPage(HttpServletRequest request) {
        // RoleBean roleBean = new RoleBean();
        // request.setAttribute("roleBean", roleBean);
        //
        // // 返回可分配的宜定盈列表
        // P2pserviceChannelBean p2pserviceChannelBean = new
        // P2pserviceChannelBean();
        // List<P2pserviceChannelBean> p2pserviceChannelBeanList = roleService
        // .getP2pserviceChannelBeanList(p2pserviceChannelBean);
        // request.setAttribute("p2pserviceChannelBeanList",
        // p2pserviceChannelBeanList);
        return "/system/roleAdd";
    }

    /**
     * 渠道配额添加页面
     *
     * @param request
     * @return
     * @Author null
     * @Date 2014-5-20 下午03:41:51
     * @Version V1.0
     *
     */
    @RequestMapping("/setRoleModulePage")
    public String setRoleModulePage(RoleBean roleBean, HttpServletRequest request) {
        RoleBean role = roleService.getRoleBean(roleBean);

        role.setOrder("");
        // ModuleBean moduleBean = new ModuleBean();
        request.setAttribute("role", role);
        //
        // // 返回可分配的宜定盈列表
        // P2pserviceChannelBean p2pserviceChannelBean = new
        // P2pserviceChannelBean();
        // List<P2pserviceChannelBean> p2pserviceChannelBeanList = moduleService
        // .getP2pserviceChannelBeanList(p2pserviceChannelBean);
        // request.setAttribute("p2pserviceChannelBeanList",
        // p2pserviceChannelBeanList);
        return "/system/roleModuleAssign";
    }

    /**
     * 添加Or修改渠道配额
     *
     * @param roleBean
     * @return
     * @Author null
     * @Date 2014-5-20 下午03:39:54
     * @Version V1.0
     *
     */
    @RequestMapping(value = "/addOrUpdateRole", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public ResponseData addOrUpdateRole(RoleBean roleBean) throws Exception {
        UserBean operator = userRedisService.getUserBeanSession(request);
        ;
        logger.info("add Or Update Role 保存宜定盈app配额信息\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ")Start");
        ResponseData jsonResponse = new ResponseData();
        boolean boo = false;
        if (roleBean == null) {
            // 返回错误
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("信息不完整");
            return jsonResponse;
        }
        if (roleBean.getId() != null && !roleBean.getId().isEmpty()) {
            // 修改操作
            roleBean.setOperatorId(operator.getId());
            boo = roleService.updateRoleBean(roleBean);
        } else {
            // 添加
            roleBean.setCreatorId(operator.getId());
            roleBean.setStatus(1);
            boo = roleService.addRoleBean(roleBean);
        }

        if (boo) {
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
     * 添加Or修改渠道配额
     *
     * @param roleBean
     * @return
     * @Author null
     * @Date 2014-5-20 下午03:39:54
     * @Version V1.0
     *
     */
    @RequestMapping(value = "/saveRoleModule", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public ResponseData saveRoleModule(RoleModuleBean roleModuleBean) throws Exception {
        UserBean operator = userRedisService.getUserBeanSession(request);
        logger.info("save Role Module \n\t@by : " + operator.getId() + "(" + operator.getUserName() + ")Start");
        ResponseData jsonResponse = new ResponseData();
        boolean boo = false;
        if (roleModuleBean == null) {
            // 返回错误
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("信息不完整");
            return jsonResponse;
        }

        RoleModuleBean roleModuleBeanCon = new RoleModuleBean();
        roleModuleBeanCon.setModuleId(roleModuleBean.getModuleId());
        roleModuleBeanCon.setRoleId(roleModuleBean.getRoleId());
        RoleModuleBean roleModuleExsits = roleModuleService.getRoleModuleBean(roleModuleBeanCon);

        if (roleModuleExsits != null) {
            // 修改操作
            roleModuleBean.setId(roleModuleExsits.getId());
            roleModuleBean.setOperatorId(operator.getId());
            boo = roleModuleService.updateRoleModuleBean(roleModuleBean);
        } else {
            // 添加
            roleModuleBean.setCreatorId(operator.getId());
            roleModuleBean.setStatus(1);
            boo = roleModuleService.addRoleModuleBean(roleModuleBean);
        }

        if (boo) {
            String roleId = roleModuleBean.getRoleId();
            String redisKey = ConstantsForRedis.MGR_ROLE_MODULE_PRE + roleId;
            redisKeyBaseDao.del(redisKey);
        }

        if (boo) {
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
     * @param roleBean
     * @return
     * @Author null
     * @Date 2014-5-20 下午03:39:54
     * @Version V1.0
     *
     */
    @RequestMapping(value = "/deleteRole", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public ResponseData deleteRole(RoleBean roleBean, HttpSession session) throws Exception {
        UserBean operator = userRedisService.getUserBeanSession(request);
        logger.info("delete Role 删除角色信息\n\t@by : " + operator.getId() + "(" + operator.getUserName() + ")Start");

        ResponseData response = new ResponseData();
        boolean boo = false;

        roleBean.setOperatorId(operator.getId());
        roleBean.setStatus(0);
        boo = roleService.updateRoleBean(roleBean);

        if (boo) {
            response.setSuccess(true);
            response.setMsg("删除成功！");
            return response;
        } else {
            response.setSuccess(false);
            response.setMsg("删除失败！");
            return response;
        }
    }

    /**
     * 查询所有角色
     *
     * @param roleBean
     * @return
     * @Author null
     * @Date 2014-5-20 下午03:39:54
     * @Version V1.0
     *
     */
    @RequestMapping(value = "/getRoleList", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public List<RoleBean> getRoleList() throws Exception {
        UserBean operator = userRedisService.getUserBeanSession(request);
        logger.info("getRoleList 查询角色信息\n\t@by : " + operator.getId() + "(" + operator.getUserName() + ")Start");
        RoleBean roleBean = new RoleBean();
        roleBean.setStatus(1);
        List<RoleBean> roleList = roleService.getRoleBeanList(roleBean);

        if (roleList != null && roleList.size() > 0) {
            return roleList;
        } else {
            return null;
        }
    }

    /**
     * 查询所有角色
     *
     * @param roleBean
     * @return
     * @Author null
     * @Date 2014-5-20 下午03:39:54
     * @Version V1.0
     *
     */
    @RequestMapping(value = "/getRole", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public RoleBean getRole(RoleBean roleBean) throws Exception {
        UserBean operator = userRedisService.getUserBeanSession(request);
        logger.info("getRole 查询角色信息\n\t@by : " + operator.getId() + "(" + operator.getUserName() + ")Start");
        roleBean.setStatus(1);
        RoleBean role = roleService.getRoleBean(roleBean);

        if (role != null) {
            return role;
        } else {
            return null;
        }
    }

}
