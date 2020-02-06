<?xml version="1.0" encoding="UTF-8" ?>

<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >

<mapper namespace="com.samples.demo.dao.${clazz.name}Dao">
    <resultMap id="${clazz.lowerCaseName}Map" type="com.samples.demo.model.${clazz.name}">
    <#list clazz.propertyList as property>
        <#if property.name == "id">
        <id property="id" column="id"/>
        <#else>
        <result property="${property.name}" column="${property.column.name}" />
        </#if>
    </#list>
    </resultMap>

    <sql id="where_if">
        <trim prefix="where" prefixOverrides="and|or">
            <if test="criteria != null">
            <#list clazz.propertyList as property>
                <if test="criteria.${property.name} != null">
                    and ${property.column.name} = ${'#'}${'{'}criteria.${property.name}${'}'}
                </if>
            </#list>
            </if>
        </trim>
    </sql>

    <insert id="insert">
        <#if clazz.table.primaryKey.isAutoincrement == "YES">
        <#assign f = 0 />
        insert into ${clazz.table.name}(<#list clazz.propertyList as property><#if property.name != "id" && property.name != "updatedAt"><#if f == 1>, </#if>${property.column.name}<#assign f = 1 /></#if></#list>)
        <#assign f = 0 />
        values(<#list clazz.propertyList as property><#if property.name != "id" && property.name != "updatedAt"><#if f == 1>, </#if>${'#'}${'{'}${property.name}${'}'}<#assign f = 1 /></#if></#list>)
        <selectKey keyProperty="id" resultType="Integer" order="AFTER" >
            select last_insert_id() as value
        </selectKey>

        <#else >
        <#assign f = 0 />
        insert into ${clazz.table.name}(<#list clazz.propertyList as property><#if property.name != "updatedAt"><#if f == 1>, </#if>${property.column.name}<#assign f = 1 /></#if></#list>)
        <#assign f = 0 />
        values(<#list clazz.propertyList as property><#if property.name != "updatedAt"><#if f == 1>, </#if>${'#'}${'{'}${property.name}${'}'}<#assign f = 1 /></#if></#list>)
        </#if>
    </insert>

    <insert id="insertBatch">
        <#if clazz.table.primaryKey.isAutoincrement == "YES">
        <#assign f = 0 />
        insert into ${clazz.table.name}(<#list clazz.propertyList as property><#if property.name != "id" && property.name != "updatedAt"><#if f == 1>, </#if>${property.column.name}<#assign f = 1 /></#if></#list>)
        <#assign f = 0 />
        values(<#list clazz.propertyList as property><#if property.name != "id" && property.name != "updatedAt"><#if f == 1>, </#if>${'#'}${'{'}${property.name}${'}'}<#assign f = 1 /></#if></#list>)
        <#else>
        <#assign f = 0 />
        insert into ${clazz.table.name}(<#list clazz.propertyList as property><#if property.name != "updatedAt"><#if f == 1>, </#if>${property.column.name}<#assign f = 1 /></#if></#list>)
        <#assign f = 0 />
        values(<#list clazz.propertyList as property><#if property.name != "updatedAt"><#if f == 1>, </#if>${'#'}${'{'}${property.name}${'}'}<#assign f = 1 /></#if></#list>)
        </#if>
    </insert>

    <update id="update">
        update ${clazz.table.name}
        <trim prefix="SET" suffixOverrides=",">
        <#list clazz.propertyList as property>
            <#if property.name != "id">
                <if test="${property.name} != null">${property.column.name} = ${'#'}${'{'}${property.name}${'}'},</if>
            </#if>
        </#list>
        </trim>
        where id = ${'#'}${'{'}id${'}'}
    </update>

    <delete id="delete">
        delete from ${clazz.table.name} where ${clazz.table.primaryKey.name} = ${'#'}${'{'}${clazz.primaryProperty.name}${'}'}
    </delete>

    <select id="find" resultMap="${clazz.lowerCaseName}Map">
        select <#list clazz.propertyList as property>${property.column.name}<#if property_has_next>, </#if></#list> from ${clazz.table.name} where id = ${'#'}${'{'}id${'}'}
    </select>
</mapper>