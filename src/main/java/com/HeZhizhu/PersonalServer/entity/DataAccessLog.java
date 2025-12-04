package com.HeZhizhu.PersonalServer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "data_access_log", schema = "personal_project")
public class DataAccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @Column(name = "operation_type", nullable = false, length = 20)
    private String operationType;

    @Column(name = "table_name", nullable = false, length = 50)
    private String tableName;

    @Column(name = "query_condition", length = 500)
    private String queryCondition;

    @ColumnDefault("1")
    @Column(name = "result", nullable = false)
    private Byte result;

    @Column(name = "client_ip", nullable = false, length = 50)
    private String clientIp;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "operation_time", nullable = false)
    private Instant operationTime;

    @Column(name = "remark", length = 500)
    private String remark;

}