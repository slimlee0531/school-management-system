package com.itheima.utils;

public class CurrentHolder {

    private static final ThreadLocal<Integer>  CURRENT_ID_LOCAL = new InheritableThreadLocal<>();

    private static final ThreadLocal<String> CURRENT_NAME_LOCAL = new InheritableThreadLocal<>();

    // ID线程类
    public static void setCurrentId(Integer employeeId) {
        CURRENT_ID_LOCAL.set(employeeId);
    }

    public static Integer getCurrentId() {
        return CURRENT_ID_LOCAL.get();
    }

    // Name线程类
    public static void setCurrentEmpName(String empName) {
        CURRENT_NAME_LOCAL.set(empName);
    }

    public static String getCurrentEmpName() {
        return CURRENT_NAME_LOCAL.get();
    }

    public static void remove() {
        CURRENT_NAME_LOCAL.remove();
        CURRENT_ID_LOCAL.remove();
    }


}
