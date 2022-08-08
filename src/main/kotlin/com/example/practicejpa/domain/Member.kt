package com.example.practicejpa.domain

import com.example.practicejpa.util.BaseEntity
import com.example.practicejpa.util.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Member(
    val pw: String,
    val email: String
): BaseEntity() {
}