package com.muabe.uniboot.core;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import dalvik.system.DexFile;

public class JwClassLoader {
	private ArrayList<Class<?>> classes;
	private String[] packageNames;

	private HashMap<String,String> packageFilter;
	private HashMap<Class<?>,String> classFilter;

	public JwClassLoader(String... packageNames) {
		this.classes = new ArrayList<>();
		this.packageNames = packageNames;
		packageFilter = new HashMap<>();
		classFilter = new HashMap<>();
	}

	public void setPackageFilter(String... packageNames){
		packageFilter.clear();
		if(packageNames!=null) {
			for (int i = 0; i < packageNames.length; i++) {
				packageFilter.put(packageNames[i], packageNames[i]);
			}
		}
	}

	public void setClassFilter(Class<?>... classes){
		classFilter.clear();
		if(classes!=null) {
			for (int i = 0; i < classes.length; i++) {
				if(classes[i]!=null) {
					classFilter.put(classes[i], classes[i].toString());
				}
			}
		}
	}

	public void removeClasses(ArrayList<Class<?>> removeList){
		classes.retainAll(removeList);
	}

	public void removePackage(){

	}

	public void clear(){
		classes.clear();
	}

	public ArrayList<Class<?>> getClasses(){
		return classes;
	}

	public ArrayList<Class<?>> load(Context context) throws IOException, ClassNotFoundException {
		for(int i=0;i<packageNames.length;i++){
			this.load(context, packageNames[i]);
		}
		return classes;
	}
	public ArrayList<Class<?>> getClassAll(){
		return classes;
	}

	public <T>ArrayList<Class<T>> getSubClassList(Class<T> classType){
		ArrayList<Class<T>> list = new ArrayList<>();
		for(int i=0;i<classes.size();i++){
			if (!packageFilter.containsKey(classes.get(i).getPackage().getName()) && instanceOf(classes.get(i), classType)) {
				if(!classFilter.containsKey(classes.get(i))) {
					list.add((Class<T>) classes.get(i));
				}
			}

		}
		return list;
	}

	private ArrayList<Class<?>> load(Context context, String packageName) throws IOException, ClassNotFoundException {
		DexFile df = new DexFile(context.getPackageCodePath());
		for (Enumeration<String> iter = df.entries(); iter.hasMoreElements();) {
			String className = iter.nextElement();
			if (className.indexOf(packageName) == 0 && className.indexOf("$") < 0 && className.length() > packageName.length()) {
				Class<?> clz = Class.forName(className);
				classes.add(clz);
			}
		}
		df.close();
		System.gc();
		return classes;
	}

	private boolean instanceOf(Class<?> targetClass, Class<?> instanceOfClass){
		targetClass = targetClass.getSuperclass();
		while(targetClass!=null && !targetClass.getName().equals("java.lang.Object")){
			if(targetClass.getName().equals(instanceOfClass.getName())){
				return true;
			}
			targetClass = targetClass.getSuperclass();
		}
		return false;
	}
}
