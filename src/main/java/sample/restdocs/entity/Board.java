package sample.restdocs.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "BOARD")
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int boardId;
	private String name;
	private int createUserId;
	private Timestamp createDate;
	private int updateUserId;
	private Timestamp updateDate;
	private boolean deleteFlg;
	private int version;

	@PrePersist
	public void onPrePersist() {
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		setCreateDate(nowTime);
		setUpdateDate(nowTime);
		setDeleteFlg(false);
	}

	@PreUpdate
	public void onPreUpdate() {
		Timestamp nowTime = new Timestamp(System.currentTimeMillis());
		setUpdateDate(nowTime);
	}
}
