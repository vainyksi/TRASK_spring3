package com.websystique.springmvc.dao;

import java.util.List;

import com.websystique.springmvc.model.Unit;

public interface UnitDao {

	void saveUnit(Unit unit);

	Unit findById(int agentId, int branchId);

	List<Unit> findAllByBranchId(int id);

	Unit findByUserId(int agentId);

	List<Unit> findAllUnits();

	void deleteById(int userId, int branchId);

}
