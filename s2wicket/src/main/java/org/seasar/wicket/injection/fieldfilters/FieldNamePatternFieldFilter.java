/*
 * $Id$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.seasar.wicket.injection.fieldfilters;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.wicket.injection.FieldFilter;

/**
 * �^����ꂽ�t�B�[���h���̃p�^�[���ɏ]���ăC���W�F�N�V�����Ώۂ̃t�B�[���h���ǂ����𔻒f����t�B�[���h�t�B���^�N���X�ł��B<br />
 * <p>���̃t�B�[���h�t�B���^���g�p���邱�Ƃɂ��C�t�B�[���h���̖����K���ɏ]���ăC���W�F�N�V�������s����悤�ɂȂ�܂��B
 * </p>
 * @author Yoichiro Tanaka
 * @since 1.2.0
 */
public class FieldNamePatternFieldFilter implements FieldFilter {
	
//	--- �p�^�[���t�B�[���h
	
	/** �p�b�P�[�W����肷�邽�߂̐��K�\���p�^�[�������� */
	private String packageNamePatternRegex;
	
	/** �N���X����肷�邽�߂̐��K�\���p�^�[�������� */
	private String classNamePatternRegex;
	
	/** �t�B�[���h����肷�邽�߂̐��K�\���p�^�[�������� */
	private String fieldNamePatternRegex;
	
	/** �p�b�P�[�W����肷�邽�߂̃R���p�C���ςݐ��K�\���p�^�[�� */
	private Pattern packageNamePattern;
	
	/** �N���X����肷�邽�߂̃R���p�C���ςݐ��K�\���p�^�[�� */
	private Pattern classNamePattern;
	
	/** �t�B�[���h����肷�邽�߂̃R���p�C���ςݐ��K�\���p�^�[�� */
	private Pattern fieldNamePattern;
	
//	--- �R���X�g���N�^

	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 */
	public FieldNamePatternFieldFilter() {
		super();
	}
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 * @param packageNamePatternRegex �p�b�P�[�W����肷�邽�߂̐��K�\���p�^�[��������
	 * @param classNamePatternRegex �N���X����肷�邽�߂̐��K�\���p�^�[��������
	 * @param fieldNamePatternRegex �t�B�[���h����肷�邽�߂̐��K�\���p�^�[��������
	 */
	public FieldNamePatternFieldFilter(
			String packageNamePatternRegex, String classNamePatternRegex, String fieldNamePatternRegex) {
		super();
		setPackageNamePatternRegex(packageNamePatternRegex);
		setClassNamePatternRegex(classNamePatternRegex);
		setFieldNamePatternRegex(fieldNamePatternRegex);
	}
	
//	--- �v���p�e�B���\�b�h
	
	/**
	 * �N���X����肷�邽�߂̐��K�\���p�^�[���������Ԃ��܂��B
	 * @return �N���X����肷�邽�߂̐��K�\���p�^�[��������
	 */
	public String getClassNamePatternRegex() {
		return classNamePatternRegex;
	}

	/**
	 * �N���X����肷�邽�߂̐��K�\���p�^�[����������Z�b�g���܂��B
	 * @param classNamePatternRegex �N���X����肷�邽�߂̐��K�\���p�^�[��������
	 */
	public void setClassNamePatternRegex(String classNamePatternRegex) {
		if (classNamePatternRegex == null)
			throw new IllegalArgumentException("classNamePatternRegex is null.");
		if (classNamePatternRegex.length() == 0)
			throw new IllegalArgumentException("classNamePatternRegex is empty.");
		classNamePattern = Pattern.compile(classNamePatternRegex);
		this.classNamePatternRegex = classNamePatternRegex;
	}

	/**
	 * �t�B�[���h����肷�邽�߂̐��K�\���p�^�[���������Ԃ��܂��B
	 * @return �t�B�[���h����肷�邽�߂̐��K�\���p�^�[��������
	 */
	public String getFieldNamePatternRegex() {
		return fieldNamePatternRegex;
	}

	/**
	 * �t�B�[���h����肷�邽�߂̐��K�\���p�^�[����������Z�b�g���܂��B
	 * @param fieldNamePatternRegex �t�B�[���h����肷�邽�߂̐��K�\���p�^�[��������
	 */
	public void setFieldNamePatternRegex(String fieldNamePatternRegex) {
		if (fieldNamePatternRegex == null)
			throw new IllegalArgumentException("fieldNamePatternRegex is null.");
		if (fieldNamePatternRegex.length() == 0)
			throw new IllegalArgumentException("fieldNamePatternRegex is empty.");
		fieldNamePattern = Pattern.compile(fieldNamePatternRegex);
		this.fieldNamePatternRegex = fieldNamePatternRegex;
	}

	/**
	 * �p�b�P�[�W����肷�邽�߂̐��K�\���p�^�[���������Ԃ��܂��B
	 * @return �p�b�P�[�W����肷�邽�߂̐��K�\���p�^�[��������
	 */
	public String getPackageNamePatternRegex() {
		return packageNamePatternRegex;
	}

	/**
	 * �p�b�P�[�W����肷�邽�߂̐��K�\���p�^�[����������Z�b�g���܂��B
	 * @param packageNamePatternRegex �p�b�P�[�W����肷�邽�߂̐��K�\���p�^�[��������
	 */
	public void setPackageNamePatternRegex(String packageNamePatternRegex) {
		if (packageNamePatternRegex == null)
			throw new IllegalArgumentException("packageNamePatternRegex is null.");
		if (packageNamePatternRegex.length() == 0)
			throw new IllegalArgumentException("packageNamePatternRegex is empty.");
		packageNamePattern = Pattern.compile(packageNamePatternRegex);
		this.packageNamePatternRegex = packageNamePatternRegex;
	}	

//	--- FieldFilter�������\�b�h
	
	/**
	 * �w�肳�ꂽ�C���W�F�N�V�����ΏۂƂȂ�t�B�[���h�ɑ΂��ăC���W�F�N�V��������R���|�[�l���g�I�u�W�F�N�g�̃R���|�[�l���g����Ԃ��܂��B
	 * ���̎����N���X�ł́C�t�B�[���h�������b�N�A�b�v����R���|�[�l���g�I�u�W�F�N�g�̃R���|�[�l���g���Ƃ��ĕԂ��܂��B
	 * @param field �C���W�F�N�V�����ΏۂƂȂ�t�B�[���h
	 * @return ���b�N�A�b�v����R���|�[�l���g�I�u�W�F�N�g�̃R���|�[�l���g��
	 * @see org.seasar.wicket.injection.FieldFilter#getLookupComponentName(java.lang.reflect.Field)
	 */
	public String getLookupComponentName(Field field) {
		if (field == null)
			throw new IllegalArgumentException("field is null.");
		return field.getName();
	}

	/**
	 * �w�肳�ꂽ�t�B�[���h���C���W�F�N�V�����Ώۂ��ǂ�����Ԃ��܂��B
	 * ���̎����N���X�ł́C{@link #setPackageNamePatternRegex(String)}�C{@link #setClassNamePatternRegex(String)}�C
	 * {@link #setFieldNamePatternRegex(String)}�̂��ꂼ��̃��\�b�h�Ŏw�肳�ꂽ���K�\���p�^�[���ɁC
	 * �w�肳�ꂽ�t�B�[���h�����v���邩�ǂ������`�F�b�N���C���̌��ʂ�Ԃ��܂��B
	 * @param field �`�F�b�N�Ώۂ̃t�B�[���h
	 * @return �C���W�F�N�V�����ΏۂƔ��f���ꂽ�ꍇ�� true
	 * @see org.seasar.wicket.injection.FieldFilter#isSupported(java.lang.reflect.Field)
	 */
	public boolean isSupported(Field field) {
		if (field == null)
			throw new IllegalArgumentException("field is null.");
		if (packageNamePattern == null)
			throw new IllegalStateException("packageNamePatternRegex not set.");
		if (classNamePattern == null)
			throw new IllegalStateException("classNamePatternRegex not set.");
		if (fieldNamePattern == null)
			throw new IllegalStateException("fieldNamePatternRegex not set.");
		Class<?> clazz = field.getDeclaringClass();
		Package pkg = clazz.getPackage();
		Matcher matcher = packageNamePattern.matcher(pkg.getName());
		if (matcher.matches()) {
			matcher = classNamePattern.matcher(clazz.getSimpleName());
			if (matcher.matches()) {
				matcher = fieldNamePattern.matcher(field.getName());
				return matcher.matches();
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
