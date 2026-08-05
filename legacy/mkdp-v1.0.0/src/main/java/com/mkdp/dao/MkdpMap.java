package com.mkdp.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;


public class MkdpMap {

	
	 int deptno;
	 String dname;
	 String loc;
		
		
	// 1. 4가지 정보

		public MkdpMap () {
			
		}
		
	/*
	 * public List<Dept> multiSelectMap(SqlSession session, HashMap<String, Integer>
	 * map) { List<Dept> list=
	 * session.selectList("com.dept.DeptMapper4.multiSelectMap", map);
	 * System.out.println(list.size()); return list; } public void
	 * multiUpdate(SqlSession session, List<Integer> deptnoes2) {
	 * session.update("com.dept.DeptMapper4.multiUpdate", deptnoes2); } public
	 * List<Dept> multiSelect(SqlSession session, List<Integer> deptnoes){
	 * List<Dept> list= session.selectList("com.dept.DeptMapper4.multiSelect",
	 * deptnoes); return list; } public List<Dept> selectDynamicChoose(SqlSession
	 * session, HashMap<String, String> map){ List<Dept>
	 * list=session.selectList("com.dept.DeptMapper3.selectDynamicChoose", map);
	 * return list; } public List<Dept> selectDynamicDeptno(SqlSession session,
	 * HashMap<String, Integer> map){ System.out.println(map.get("deptno"));
	 * List<Dept> list=
	 * session.selectList("com.dept.DeptMapper2.selectDynamicDeptno", map); return
	 * list; } public List<Dept> selectByHashMap(SqlSession session, HashMap<String,
	 * Integer> map) { List<Dept> list= session.selectList("selectByHashMap", map);
	 * return list; } public HashMap selectByDeptnoHashMap(SqlSession session,int
	 * deptno) { HashMap map= session.selectOne("selectByDeptHashmap", deptno);
	 * return map; } public int deptRecordCount(SqlSession session) { int num=
	 * session.selectOne("deptRecordCount"); return num; } public void
	 * delete(SqlSession session, int deptno) { int num =
	 * session.delete("deptDelete", deptno); System.out.println("삭제된 갯수 ====" +
	 * num); }
	 * 
	 * public Dept selectByDeptno(SqlSession session, int deptno) { Dept dept =
	 * session.selectOne("com.dept.DeptMapper.selectByDeptno", deptno); return dept;
	 * }
	 * 
	 * public List<Dept> selectAll(SqlSession session) { // <select id="selectAll"
	 * resultType="com.dto.dept"> // List<Dept> list=
	 * session.selectList("selectAll"); List<Dept> list =
	 * session.selectList("com.dept.DeptMapper2.selectAll"); return list; }
	 * 
	 * public void insert(SqlSession session, Dept dept) { int num =
	 * session.insert("com.dept.DeptMapper.deptInsert", dept);
	 * System.out.println("추가된 레코드 ====" + num); }
	 * 
	 * public void update(SqlSession session, Dept dept) { int num =
	 * session.update("com.dept.DeptMapper.deptUpdate", dept);
	 * System.out.println("업데이트 갯수====" + num); }
	 */
		
}
