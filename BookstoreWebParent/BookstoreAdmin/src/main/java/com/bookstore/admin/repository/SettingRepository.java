package com.bookstore.admin.repository;

import com.bookstore.common.entity.Setting;
import com.bookstore.common.entity.SettingCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting, String> {
    List<Setting> findByCategory(SettingCategory category);
}