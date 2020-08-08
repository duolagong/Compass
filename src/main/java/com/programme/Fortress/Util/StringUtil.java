package com.programme.Fortress.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 去空格（验签使用）
     * @param message
     * @return
     */
    public String filterMessage(String message){
        Pattern p = Pattern.compile("\\s*|\r|\n");
        Matcher m = p.matcher(message);
        message = m.replaceAll("");
        return message;
    }

    /**
     * Xml格式化
     * @param str
     * @return
     * @throws Exception
     */
    public static String xmlFormat(String str) throws Exception {
        SAXReader reader = new SAXReader();
        StringReader in = new StringReader(str);
        Document doc = reader.read(in);
        OutputFormat formater = OutputFormat.createPrettyPrint();
        formater.setEncoding("utf-8");
        StringWriter out = new StringWriter();
        XMLWriter writer = new XMLWriter(out, formater);
        writer.write(doc);
        writer.close();
        return out.toString();
    }

    /**
     * Clob转String
     * @param tranxmlClob
     * @return
     * @throws SQLException
     */
    public static String clobToString(Clob tranxmlClob) throws SQLException {
        return tranxmlClob.getSubString(1, (int) tranxmlClob.length());
    }

    /**
     * 校验是否为空
     * @param obj
     * @return
     */
    public static boolean checkEmpty(Object obj) {
        return obj == null || "".equals(obj.toString()) || "null".equals(obj.toString());
    }
}
