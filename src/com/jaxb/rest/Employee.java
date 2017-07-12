package com.jaxb.rest;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "employees")
public class Employee {
	String name;
	int age;
	String id;
	String type;

	public String getName() {
		return name;
	}

	@XmlElement(name="name")
	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return name;
	}

	@XmlElement(name="id")
	public void setId(String id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	@XmlElement(name="age")
	public void setAge(int age) {
		this.age = age;
	}

	public String getType() {
		return type;
	}

	@XmlAttribute(name = "type")
	public void setType(String type) {
		this.type = type;
	}

}
