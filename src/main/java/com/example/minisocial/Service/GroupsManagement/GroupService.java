package com.example.minisocial.Service.GroupsManagement;


import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.GroupsManagement.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;

    @Stateless
    public class GroupService {

        @PersistenceContext(unitName = "myPersistenceUnit")
        private EntityManager em;

        public void save(Group group) {
            em.persist(group);
        }

        public void update(Group group) {
            em.merge(group);
        }
//for lazy initialize
        public Group findGroup(Long id) {
            return em.createQuery(
                            "SELECT g FROM Group g LEFT JOIN FETCH g.members WHERE g.id = :id", Group.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }


        public User findUser(Long id) {
            return em.find(User.class, id);
        }
    }

