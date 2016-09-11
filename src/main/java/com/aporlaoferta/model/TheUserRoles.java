package com.aporlaoferta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 14/01/2015
 * Time: 17:58
 */
@Entity
@Table(name = "thatuserroles")
@SequenceGenerator(name = "GEN_THATUSERROLES", sequenceName = "SEQ_THATUSERROLES")
public class TheUserRoles implements Serializable {

    private static final long serialVersionUID = -4434426496751728489L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TUR_ID")
    private Long id;

    @Version
    @Column(name = "TUR_VERSION_ID")
    private Long version;

    @Column(name = "TUR_NICKNAME", nullable = false)
    private String userNickname;

    @Column(name = "TUR_ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoles userRole;

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public UserRoles getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoles userRole) {
        this.userRole = userRole;
    }

}
