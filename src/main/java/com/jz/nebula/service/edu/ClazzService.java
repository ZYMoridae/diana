/*
 * Copyright (c) 2019. Nebula Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jz.nebula.service.edu;

import com.google.common.base.Strings;
import com.jz.nebula.controller.api.UserController;
import com.jz.nebula.controller.api.edu.ClazzController;
import com.jz.nebula.dao.UserRepository;
import com.jz.nebula.dao.edu.ClazzCategoryRepository;
import com.jz.nebula.dao.edu.ClazzRepository;
import com.jz.nebula.dao.edu.TeacherAvailableTimeRepository;
import com.jz.nebula.dao.edu.UserClazzRatingRepository;
import com.jz.nebula.dto.edu.ClazzParam;
import com.jz.nebula.entity.User;
import com.jz.nebula.entity.edu.Clazz;
import com.jz.nebula.entity.edu.ClazzCategory;
import com.jz.nebula.entity.edu.TeacherAvailableTime;
import com.jz.nebula.entity.edu.UserClazzRating;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class ClazzService {
    private final static Logger logger = LogManager.getLogger(ClazzService.class);


    private ClazzRepository clazzRepository;


    private TeacherAvailableTimeRepository teacherAvailableTimeRepository;


    private ClazzCategoryRepository clazzCategoryRepository;


    private UserClazzRatingRepository userClazzRatingRepository;

    private UserRepository userRepository;

    @Autowired
    public ClazzService(ClazzRepository clazzRepository,
                        TeacherAvailableTimeRepository teacherAvailableTimeRepository,
                        ClazzCategoryRepository clazzCategoryRepository,
                        UserClazzRatingRepository userClazzRatingRepository,
                        UserRepository userRepository) {
        this.clazzRepository = clazzRepository;
        this.teacherAvailableTimeRepository = teacherAvailableTimeRepository;
        this.clazzCategoryRepository = clazzCategoryRepository;
        this.userClazzRatingRepository = userClazzRatingRepository;
        this.userRepository = userRepository;
    }

    /**
     * Find all classes
     *
     * @param keyword
     * @param pageable
     * @param assembler
     *
     * @return
     */
    public PagedModel<EntityModel<Clazz>> findAll(long clazzCategoryId, String keyword, Pageable pageable,
                                                  PagedResourcesAssembler<Clazz> assembler) {
        Page<Clazz> page;
        if (Strings.isNullOrEmpty(keyword)) {
            page = clazzRepository.findAll(pageable);
            logger.debug("findAll::order by id");
        } else {
            page = clazzRepository.findAll(pageable);
//            page = clazzRepository.findByClazzCategoryIdAndNameContaining(clazzCategoryId, keyword, pageable);
            logger.debug("findAll::find by name containing");
        }

        PagedModel<EntityModel<Clazz>> resources = assembler.toModel(page,
                linkTo(UserController.class).slash("/classes").withSelfRel());

        return resources;
    }

    /**
     * Find by id
     *
     * @param id
     *
     * @return
     */
    public Clazz findById(long id) {
        return clazzRepository.findById(id).get();
    }

    /**
     * Native save class
     *
     * @param clazz
     *
     * @return
     */
    public Clazz save(Clazz clazz) {
        Clazz persistedClazz = clazzRepository.save(clazz);
        return findById(persistedClazz.getId());
    }

    public Clazz create(ClazzParam clazzParam) {
        Clazz clazz = new Clazz();
        BeanUtils.copyProperties(clazzParam, clazz);

        setTeacher(clazz, clazzParam);
        setClazzCategory(clazz, clazzParam);

        Clazz persistedClazz = clazzRepository.save(clazz);
        return findById(persistedClazz.getId());
    }

    public Clazz update(long id, ClazzParam clazzParam) {
        Clazz clazz = new Clazz();
        clazz.setId(id);
        BeanUtils.copyProperties(clazzParam, clazz);

        setTeacher(clazz, clazzParam);
        setClazzCategory(clazz, clazzParam);

        Clazz persistedClazz = clazzRepository.save(clazz);
        return findById(persistedClazz.getId());
    }

    private void setTeacher(Clazz clazz, ClazzParam clazzParam) {
        if (clazzParam.getTeacherId() != null) {
            User teacher = userRepository.findById(clazzParam.getTeacherId()).get();
            clazz.setTeacher(teacher);
        }
    }

    private void setClazzCategory(Clazz clazz, ClazzParam clazzParam) {
        if (clazzParam.getClazzCategoryId() != null) {
            ClazzCategory clazzCategory = clazzCategoryRepository.findById(clazzParam.getClazzCategoryId()).get();
            clazz.setClazzCategory(clazzCategory);
        }
    }

    /**
     * Delete class by id
     *
     * @param id
     */
    public void delete(long id) {
        clazzRepository.deleteById(id);
    }

    /**
     * Create teacher available time
     *
     * @param teacherAvailableTime
     *
     * @return
     */
    public TeacherAvailableTime createTeacherAvailableTime(TeacherAvailableTime teacherAvailableTime) {
        return teacherAvailableTimeRepository.save(teacherAvailableTime);
    }

    /**
     * Delete teacher available time by id
     *
     * @param id
     */
    public void deleteTeacherAvailableTimeById(Long id) {
        teacherAvailableTimeRepository.deleteById(id);
    }

    /**
     * Update teacher available time
     *
     * @param teacherAvailableTime
     *
     * @return
     */
    public TeacherAvailableTime saveTeacherAvailableTime(TeacherAvailableTime teacherAvailableTime) {
        return teacherAvailableTimeRepository.save(teacherAvailableTime);
    }

    /**
     * Find teacher available time by id
     *
     * @param id
     *
     * @return
     */
    public TeacherAvailableTime findTeacherAvailableTimeById(long id) {
        return teacherAvailableTimeRepository.findById(id).get();
    }

    /**
     * Find class category by id
     *
     * @param id
     *
     * @return
     */
    public ClazzCategory findClazzCategoryById(long id) {
        return clazzCategoryRepository.findById(id).get();
    }

    /**
     * Save class category
     *
     * @param clazzCategory
     *
     * @return
     */
    public ClazzCategory saveClazzCategory(ClazzCategory clazzCategory) {
        return clazzCategoryRepository.save(clazzCategory);
    }

    /**
     * Delete class category by id
     *
     * @param id
     */
    public void deleteClazzCategoryById(long id) {
        clazzCategoryRepository.deleteById(id);
    }

    /**
     * Find all class category
     *
     * @param keyword
     * @param pageable
     * @param assembler
     *
     * @return
     */
    public PagedModel<EntityModel<ClazzCategory>> findAllClazzCategory(String keyword, Pageable pageable,
                                                                       PagedResourcesAssembler<ClazzCategory> assembler) {
        Page<ClazzCategory> page;
        if (Strings.isNullOrEmpty(keyword)) {
            page = clazzCategoryRepository.findAll(pageable);
        } else {
            page = clazzCategoryRepository.findByNameContaining(keyword, pageable);
        }
        logger.debug("findAllClazzCategory::find clazz category totally [{}]", page.getTotalElements());

        PagedModel<EntityModel<ClazzCategory>> resources = assembler.toModel(page,
                linkTo(ClazzController.class).slash("/classes/categories").withSelfRel());
        ;
        return resources;
    }

    /**
     * Save user class rating
     *
     * @param userClazzRating
     */
    @Transactional(rollbackFor = {Exception.class})
    public synchronized void saveUserClazzRating(UserClazzRating userClazzRating) {
        userClazzRatingRepository.save(userClazzRating);
        logger.debug("saveUserClazzRating::user clazz rating has been saved");

        Clazz clazz = clazzRepository.findById(userClazzRating.getClazzId()).get();

        double totalRating = clazz.getRating() * clazz.getRatingCount();

        clazz.setRating(totalRating / (clazz.getRatingCount() + 1));
        clazz.setRatingCount(clazz.getRatingCount() + 1);

        clazzRepository.save(clazz);
        logger.debug("saveUserClazzRating::clazz rating has been updated");
    }

    /**
     * Delete user clazz rating
     *
     * @param userId
     * @param clazzId
     *
     * @throws Exception
     */
    @Transactional(rollbackFor = {Exception.class})
    public synchronized void deleteUserClazzRating(long userId, long clazzId) throws Exception {
        Optional<UserClazzRating> userClazzRating = userClazzRatingRepository.findByUserIdAndClazzId(userId, clazzId);

        if (!userClazzRating.isPresent()) {
            throw new Exception("User rating can not be found!");
        }

        userClazzRatingRepository.delete(userClazzRating.get());
    }
}
