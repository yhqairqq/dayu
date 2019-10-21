package com.caicai.ottx.service.utils;

/**
 * Created by huaseng on 2019/10/8.
 */
public class JobModule {

    public static final String model = "{\n" +
            "  \"job\": {\n" +
            "    \"setting\": {\n" +
            "      \"speed\": {\n" +
            "        \"channel\": 1\n" +
            "      }\n" +
            "    },\n" +
            "    \"content\": [{\n" +
            "      \"reader\": {\n" +
            "        \"name\": \"mysqlreader\",\n" +
            "        \"parameter\": {\n" +
            "          \"username\": \"sync\",\n" +
            "          \"password\": \"NuhEL2@p90BT\",\n" +
            "          \"connection\": [{\n" +
            "            \"querySql\": [\n" +
            "              \"select * from erp_warehouse order by create_time;\"\n" +
            "            ],\n" +
            "            \"jdbcUrl\": [\n" +
            "              \"jdbc:mysql://db-prod00-erp-1:3306/erp\"\n" +
            "            ]\n" +
            "          }]\n" +
            "        }\n" +
            "      },\n" +
            "      \"writer\": {\n" +
            "        \"name\": \"mysqlwriter\",\n" +
            "        \"parameter\": {\n" +
                "      \"writeMode\": \"insert\",\n" +
            "          \"username\": \"realtime_db\",\n" +
            "          \"password\": \"95wO0e00HwZ93xno\",\n" +
            "          \"dateFormat\": \"YYYY-MM-dd hh:mm:ss\",\n" +
            "          \"column\": [\n" +
            "            \"id\",\n" +
            "            \"city_id\",\n" +
            "            \"name\",\n" +
            "            \"code\",\n" +
            "            \"type\",\n" +
            "            \"status\",\n" +
            "            \"province\",\n" +
            "            \"city\",\n" +
            "            \"county\",\n" +
            "            \"address\",\n" +
            "            \"linkman_name\",\n" +
            "            \"phone\",\n" +
            "            \"linkman_id\",\n" +
            "            \"create_user_id\",\n" +
            "            \"create_time\",\n" +
            "            \"memo\",\n" +
            "            \"longitude\",\n" +
            "            \"latitude\"\n" +
            "          ],\n" +
            "          \"session\": [\n" +
            "            \"set session sql_mode='ANSI'\"\n" +
            "          ],\n" +
            "          \"preSql\":[\n" +
            "            \"delete from erp_warehouse\"\n" +
            "          ],\n" +
            "          \"connection\": [{\n" +
            "            \"jdbcUrl\": \"jdbc:mysql://db-realtime:3306/realtime_db\",\n" +
            "            \"table\": [\n" +
            "              \"erp_warehouse\"\n" +
            "            ]\n" +
            "          }]\n" +
            "        }\n" +
            "      }\n" +
            "    }]\n" +
            "  }\n" +
            "}";
}
