package com.websystique.springmvc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Note")
public class Note implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@DateTimeFormat(pattern = "dd.MM.yyyy")
	@Column(name = "created_date", nullable = true)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate createdDate;

	@Size(max = 45)
	@Column(name = "text", nullable = true)
	private String text;

	@Id
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Registration.class)
	@JoinColumns({ @JoinColumn(name = "registration_ico", nullable = false),
			@JoinColumn(name = "registration_reg_date", nullable = false) })
	private Registration registration;
}
