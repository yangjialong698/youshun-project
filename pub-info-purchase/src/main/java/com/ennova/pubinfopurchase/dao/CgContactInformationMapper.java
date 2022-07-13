package com.ennova.pubinfopurchase.dao;

import com.ennova.pubinfopurchase.entity.CgContactInformation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CgContactInformationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cg_contact_information
     *
     * @mbg.generated Tue Jul 12 14:06:18 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cg_contact_information
     *
     * @mbg.generated Tue Jul 12 14:06:18 CST 2022
     */
    int insert(CgContactInformation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cg_contact_information
     *
     * @mbg.generated Tue Jul 12 14:06:18 CST 2022
     */
    int insertSelective(CgContactInformation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cg_contact_information
     *
     * @mbg.generated Tue Jul 12 14:06:18 CST 2022
     */
    CgContactInformation selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cg_contact_information
     *
     * @mbg.generated Tue Jul 12 14:06:18 CST 2022
     */
    int updateByPrimaryKeySelective(CgContactInformation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cg_contact_information
     *
     * @mbg.generated Tue Jul 12 14:06:18 CST 2022
     */
    int updateByPrimaryKey(CgContactInformation record);
}