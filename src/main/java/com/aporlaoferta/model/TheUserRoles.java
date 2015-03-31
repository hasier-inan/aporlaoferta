package com.aporlaoferta.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TheUserRoles that = (TheUserRoles) o;

        if (userNickname != null ? !userNickname.equals(that.userNickname) : that.userNickname != null) return false;
        if (userRole != null ? !userRole.equals(that.userRole) : that.userRole != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userNickname != null ? userNickname.hashCode() : 0;
        result = 31 * result + (userRole != null ? userRole.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TheUserRoles{");
        sb.append("id=").append(id);
        sb.append(", version=").append(version);
        sb.append(", userNickname='").append(userNickname).append('\'');
        sb.append(", userRole=").append(userRole);
        sb.append('}');
        return sb.toString();
    }
}
