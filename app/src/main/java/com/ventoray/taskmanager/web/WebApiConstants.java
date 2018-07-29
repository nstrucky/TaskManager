package com.ventoray.taskmanager.web;

import com.ventoray.taskmanager.BuildConfig;

/**
 * Created by Nick on 7/8/2018.
 */

public class WebApiConstants {

    //TODO remove this and use dynamic keys when user logs in maybe
    public static final String TEST_API_KEY = BuildConfig.TEST_API_KEY;
    public static final String TEST_SERVER_IP_ADDRESS = BuildConfig.TEST_SERVER_IP_ADDRESS;

    public static final String BASE_URL = "https://"+TEST_SERVER_IP_ADDRESS+"/task_manager/v1";

    class HttpPath {
        public static final String TASKS = "tasks";
        public static final String REGISTER = "register";
        public static final String LOGIN = "login";
//        public static final String SINGLE_TASK = "tasks/:id";
    }

    class JSONResponseKey {
        static final String MESSAGE = "message";
        static final String ERROR = "error";
        static final String TASKS = "tasks";
        static final String TASK_ID = "task_id";
    }

    class HttpParam {
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String TASK = "task";
    }

    class HttpMethod {
        public static final String POST = "POST";
        public static final String GET = "GET";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
    }

    class DbVariables {
        public static final String UNIQUE_ID = "unique_id";
        public static final String TASK_STATUS = "status";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String CREATED_AT = "created_at";
        public static final String PASSWORD_HASH = "password_hash";
        public static final String API_KEY = "api_key";
        public static final String TASK = "task";
        public static final String USER_ID = "user_id";
        public static final String TASK_ID = "task_id";
    }






}
