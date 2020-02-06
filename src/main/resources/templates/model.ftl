package com.samples.demo.model;

/**
 * ${clazz.comment}实体类.
 */
public class ${clazz.name} {
    private static final long serialVersionUID = 1L;

    <#list clazz.propertyList as property>
    /**
    * ${property.comment}
    */
    private ${property.type} ${property.name};
        <#if property_has_next>

        </#if>
    </#list>

    <#list clazz.propertyList as property>
    /**
    * ${property.comment}的设置方法.
    */
    public void set${property.upperCaseName}(${property.type} ${property.name}) {
        this.${property.name} = ${property.name};
    }

    /**
    * ${property.comment}的取得方法.
    */
    public ${property.type} get${property.upperCaseName}() {
        return this.${property.name};
    }
    <#if property_has_next>

    </#if>
    </#list>
}
