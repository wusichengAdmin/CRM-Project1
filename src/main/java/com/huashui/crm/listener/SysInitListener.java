package com.huashui.crm.listener;

import com.huashui.crm.settings.domain.DicValue;
import com.huashui.crm.settings.service.DicService;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * 华水吴彦祖
 * 2020/2/14
 */

/*

   我们自定义一个监听器
   用来监听application对象

   你监听什么就需要实现哪个响应的接口
   例如我们现在要监听的是application对象，那么我们就需要实现ServletContextListener接口

   web三大组件
   Servlet（Controller）
   Filter
   Listener

   以上三大组件，统一由服务器帮我们new对象

 */

public class SysInitListener implements ServletContextListener {

    /*
      该方法是用来监听application对象创建的方法
      一旦application对象创建了，则会立即执行该方法
      反过来讲，一旦该方法执行了，说明application对象创建了

      参数：event
          通过该参数可以取得我们监听的域对象
          例如我们现在监听的是application对象，那么我们就可以使用event参数get得到application对象

    * */

    @Override
    public void contextInitialized(ServletContextEvent event) {

        /*ServletContext application = event.getServletContext();
        System.out.println("全局域对象创建了，创建的application对象是：" + application);*/


        /*
          我们需要连接数据库，将数据字典值表中的所有数据取得
          保存到application域对象中(application.setAttribute),服务器启动完毕。
          在服务器运行期间，由于application域对象始终存在
          那么我们就可以在所有的Servlet（springmvc的controller）和jsp中取得application域对象中所保存的数据字典值
          一旦服务器关闭，application域对象销毁，里面保存的数据字典值才会移除掉

        * */


        /*

           我们虽然用的是Listener,不是Controller，但是他们都是属于web开发的组件
           都是属于开发顶层的组件，一定要严格按照以mvc思想为基础的分层开发来进行操作
           需求必须调用业务层来实现

         */

        /*
           自己new的service用不了
           因为service是交给spring做管理了
           自己new出来的service对象，不能使用其中的dao属性
        * */

        /*

            我们需要使用WebApplicationContextUtils工具类，该工具类的作用是获取到spring容器的引用，进而获取到我们需要的bean实例

         */

        ServletContext application = event.getServletContext();

        DicService dicService = WebApplicationContextUtils.getWebApplicationContext(application).getBean(DicService.class);

        /*

            我们应该分门别类的查询列表，分别保存到application当中

            List<DicValue> dvList1 = 执行SQL语句 select * FROM tbl_dic_value where typeCode='appellation' 5条

            List<DicValue> dvList2 = 执行SQL语句 select * FROM tbl_dic_value where typeCode='clueState' 7条

            List<DicValue> dvList2 = 执行SQL语句 select * FROM tbl_dic_value where typeCode='returnPriority' 5条

            ...
            ...

            dvList7

            以上7个list，加在一起才是总共的47条

         */

        Map<String, List<DicValue>> map = dicService.getAll();

        /*

           将map中的key和value拆解出来
           保存到application中就可以了

        * */

        Set<String> set = map.keySet();

        for (String key : set) {

            application.setAttribute(key, map.get(key));

        }


        /*

            转码工具：
                \jdk\bin\native2ascii.exe

            处理阶段和可能性之间的对应关系

            解析Stage2Possibility.properties文件

            要将Stage2Possibility.properties文件中的键值对转换为java中的键值对map
            将map保存到application域对象中

         */

        /*

            使用ResourceBundle来解析properties文件
            指定文件的名字，一定不要加后缀名.properties

         */

        Map<String,String> pMap = new HashMap<>();

        ResourceBundle rb = ResourceBundle.getBundle("properties/Stage2Possibility2");

        Enumeration<String> e = rb.getKeys();

        while(e.hasMoreElements()){

            //取得阶段
            String key = e.nextElement();
            //取得可能性
            String value = rb.getString(key);

            System.out.println("key:"+key);
            System.out.println("value:"+value);
            System.out.println("--------------------");

            pMap.put(key,value);

        }

        //pMap:阶段和可能性对应的9组键值对
        //将pMap保存到application中，以缓存形式呈现
        application.setAttribute("pMap",pMap);

    }

}
