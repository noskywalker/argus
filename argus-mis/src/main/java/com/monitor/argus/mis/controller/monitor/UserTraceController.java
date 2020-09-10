package com.monitor.argus.mis.controller.monitor;

import com.monitor.argus.bean.usertrace.UserTraceConfigEntity;
import com.monitor.argus.bean.usertrace.UserTraceEntity;
import com.monitor.argus.bean.usertrace.UserTraceGroupEntity;
import com.monitor.argus.dao.usertrace.impl.UserTraceDaoImpl;
import com.monitor.argus.service.usertrace.IUserTraceService;
import com.monitor.argus.common.model.Json;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.common.util.excel.ExportExcel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * Created by usr on 2017/4/6.
 */
@Controller
@RequestMapping("/usertrace")
public class UserTraceController {

    private static Logger logger = LoggerFactory.getLogger(UserTraceController.class);

    @Autowired
    private IUserTraceService userTraceService;

    @RequestMapping(value = "/getAllUsertraceView", method = RequestMethod.GET)
    public String getAllUsertraceView(Model model, String hanId) {
        model.addAttribute("ylsysid", "");
        model.addAttribute("hanId", hanId);
        return "usertrace/allUsertrace";
    }

    @RequestMapping(value = "/getAllUsertrace", method = RequestMethod.GET)
    public String getAllUsertrace(Model model, String hanId) {
        List<UserTraceEntity> list = userTraceService.getAllUsertrace();
        model.addAttribute("searchCount", list.size());
        model.addAttribute("searchResults", list);
        model.addAttribute("ylsysid", "");
        model.addAttribute("hanId", hanId);
        return "usertrace/allUsertrace";
    }

    @RequestMapping(value = "/usertraceHzByDayView", method = RequestMethod.GET)
    public String usertraceHzByDayView(Model model, String hanId) {
        model.addAttribute("ylsysid", "");
        model.addAttribute("hanId", hanId);
        return "usertrace/usertraceHzByDay";
    }

    @RequestMapping(value = "/usertraceHzByDay", method = RequestMethod.GET)
    public String usertraceHzByDay(Model model, String hanId, String beginDate) {
        String searchTime = DateUtil.getSimpleDateTimeStr(new Date());
        if (!StringUtil.isEmpty(beginDate)) {
            searchTime = beginDate;
        }
        List<UserTraceGroupEntity> list = userTraceService.usertraceHzByDay(searchTime);
        if (CollectionUtils.isNotEmpty(list)) {
            UserTraceDaoImpl.noUrl.clear();
            for (UserTraceGroupEntity userTraceGroupEntity : list) {
                String userTrace = userTraceGroupEntity.getUserTrace();
                String urlNames = getUrlName(userTrace);
                userTraceGroupEntity.setUserTrace(urlNames);
            }
        }

        model.addAttribute("searchTime", searchTime);
        model.addAttribute("searchCount", list.size());
        model.addAttribute("searchResults", list);
        model.addAttribute("ylsysid", "");
        model.addAttribute("hanId", hanId);
        return "usertrace/usertraceHzByDay";
    }

    private String getUrlName(String userTrace) {
        StringBuffer urlNames = new StringBuffer();
        if (!StringUtil.isEmpty(userTrace)) {
            String[] urls = StringUtil.split(userTrace, ",");
            if (urls != null && urls.length > 0) {
                for (String url : urls) {
                    if (!StringUtil.isEmpty(url)) {
                        String urlName = UserTraceDaoImpl.urlMapping.get(url);
                        if (!StringUtil.isEmpty(urlName)) {
                            urlNames.append(urlName + "--->");
                        } else {
                            urlNames.append(url + "--->");
                            UserTraceDaoImpl.noUrl.add(url);
                        }
                    }
                }
            }
        }
        return urlNames.toString();
    }

    @RequestMapping(value = "/getUsertraceNoUrl", method = RequestMethod.GET)
    public String getUsertraceNoUrl(Model model, String hanId) {

        model.addAttribute("searchCount", UserTraceDaoImpl.noUrl.size());
        model.addAttribute("searchResults", UserTraceDaoImpl.noUrl);
        model.addAttribute("ylsysid", "");
        model.addAttribute("hanId", hanId);
        return "usertrace/getUsertraceNoUrl";
    }

    @RequestMapping("/exportUsertraceHzByDay")
    @ResponseBody
    public void exportUsertraceHzByDay(String beginDate, HttpSession session, ServletResponse servletResponse) {
        String searchTime = DateUtil.getSimpleDateTimeStr(new Date());
        if (!StringUtil.isEmpty(beginDate)) {
            searchTime = beginDate;
        }
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            ServletOutputStream out = response.getOutputStream();
            List<UserTraceGroupEntity> list = userTraceService.usertraceHzByDay(searchTime);
            if (CollectionUtils.isNotEmpty(list)) {
                UserTraceDaoImpl.noUrl.clear();
                Integer i = 1;
                for (UserTraceGroupEntity userTraceGroupEntity : list) {
                    String userTrace = userTraceGroupEntity.getUserTrace();
                    String urlNames = getUrlName(userTrace);
                    userTraceGroupEntity.setUserTrace(urlNames);
                    userTraceGroupEntity.setId(i++);
                }
            }
            response.reset();
            response.setContentType("application/x-download;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=UsertraceHzByDay.xls");
            String path = session.getServletContext().getRealPath("/");
            String fileUrl = path + "templates/" + "UsertraceHzByDay.xls";
            ExportExcel ee = new ExportExcel(fileUrl);
            InputStream is = ee.export(4, 3, list, UserTraceGroupEntity.class);

            byte[] buffer = new byte[1024];
            int count;
            while ((count = is.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/clearUsertraceHzByDay", method = RequestMethod.GET)
    public String clearUsertraceHzByDay(Model model, String hanId) {
        userTraceService.deleteAllUsertrace();
        model.addAttribute("ylsysid", "");
        model.addAttribute("hanId", hanId);
        return "usertrace/usertraceHzByDay";
    }

    @RequestMapping(value = "/userTraceUrlConfigView", method = RequestMethod.GET)
    public String userTraceUrlConfigView(Model model, String hanId) {
        model.addAttribute("ylsysid", "");
        model.addAttribute("hanId", hanId);
        return "usertrace/usertraceUrlConfig";
    }
    @RequestMapping(value = "/usertraceurlconfig", method = RequestMethod.GET)
    public String usertraceurlconfig(Model model, String hanId) {
        List<UserTraceConfigEntity> list = userTraceService.getUserTraceConfig();
        model.addAttribute("searchResults", list);
        model.addAttribute("ylsysid", "");
        model.addAttribute("hanId", hanId);
        return "usertrace/usertraceUrlConfig";
    }

    /**
     * 导入
     */
    @ResponseBody
    @RequestMapping(value = "/importConfig", method = RequestMethod.POST, headers = {"content-type=multipart/form-data"})
    public Json importConfig(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "upload") MultipartFile[] buildInfo)
            throws ServletException, IOException {
        Json jsonResponse = new Json();
        // 得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        String savePath = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");
        savePath = savePath.replace("file:", ""); // 去掉file:
        File file1 = new File(savePath);
        // 判断上传文件的保存目录是否存在
        if (!file1.exists() && !file1.isDirectory()) {
            logger.info(savePath + "目录不存在，需要创建");
            file1.mkdir();
        }
        // 删除此路径下的所有文件以及文件夹
        //delAllFile(savePath);
        try {
            InputStream is = buildInfo[0].getInputStream();// 多文件也适用,我这里就一个文件
            byte[] b = new byte[(int) buildInfo[0].getSize()];
            int read = 0;
            int i = 0;
            while ((read = is.read()) != -1) {
                b[i] = (byte) read;
                i++;
            }
            is.close();
            String filePath = savePath + "/" + "temp" + "_" + buildInfo[0].getOriginalFilename();
            logger.info("临时文件保存路径：" + savePath + "/" + "temp" + "_" + buildInfo[0].getOriginalFilename());
            OutputStream os = new FileOutputStream(new File(savePath + "/" + "temp" + "_" + buildInfo[0].getOriginalFilename()));// 文件原名,如a.txt
            os.write(b);
            os.flush();
            os.close();
            Integer count = userTraceService.importUserTraceConfig(filePath);
            if (count > 0) {
                jsonResponse.setObj(count);
                jsonResponse.setMsg("保存成功。共插入" + count + "条数据");
                jsonResponse.setSuccess(true);
                return jsonResponse;
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled())
                logger.debug("系统异常", e);
        }
        jsonResponse.setMsg("保存失败,请重试");
        jsonResponse.setSuccess(false);
        return jsonResponse;
    }


}
