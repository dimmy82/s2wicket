package org.seasar.wicket.uifactory;

import static org.seasar.wicket.utils.Gadget.isWicketClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import wicket.Component;

/**
 * 
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
class BuildingProcessor {
	
//	--- �r���_�[�t�B�[���h
	
	/** ���f���r���_�[ */
	private ModelBuilder modelBuilder;
	
	/** �R���|�[�l���g�r���_�[ */
	private ComponentBuilder componentBuilder;
	
//	--- �R���X�g���N�^
	
	/**
	 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
	 */
	BuildingProcessor() {
		super();
		// �r���_�[�𐶐�
		modelBuilder = new ModelBuilder();
		componentBuilder = new ComponentBuilder();
	}
	
//	--- �r���h�֘A���\�b�h
	
	/**
	 * �w�肳�ꂽ�R���|�[�l���g�������f���t�B�[���h����уR���|�[�l���g�t�B�[���h�ɂ��āC�\�z���s���܂��B
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 */
	void build(Component target) {
		// ���f���̍\�z
		Map<Field, Object> modelMap = buildModel(target);
		// �R���|�[�l���g�̍\�z
		buildComponent(target, modelMap);
	}
	
//	--- ���f���r���h�֘A���\�b�h
	
	/**
	 * �w�肳�ꂽ�R���|�[�l���g�����e���f���t�B�[���h�ɑ΂��āC���f���I�u�W�F�N�g���\�z���ăZ�b�g���܂��B
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @return ���f�����ƃ��f���I�u�W�F�N�g���΂Ŋi�[���ꂽ�R���N�V����
	 */
	private Map<Field, Object> buildModel(Component target) {
		// �����������f���I�u�W�F�N�g���i�[����R���N�V�����𐶐�
		Map<Field, Object> result = new HashMap<Field, Object>();
		// ���f���t�B�[���h�𒊏o
		Field[] targetFields = getTargetModelFields(target);
		// �t�B�[���h���ɏ���
		for (int i = 0; i < targetFields.length; i++) {
			// �t�B�[���h�̒l��null���`�F�b�N
			try {
				// ���f���I�u�W�F�N�g���擾
				Object modelObj = targetFields[i].get(target);
				if (modelObj == null) {
					// �t�B�[���h�l�Ƃ��郂�f���I�u�W�F�N�g�̐��������f���r���_�[�Ɉ˗�
					modelObj = modelBuilder.build(targetFields[i], target);
					// ���f���t�B�[���h�ɃZ�b�g
					targetFields[i].set(target, modelObj);
				}
				// �R���N�V�����ɒǉ�
				result.put(targetFields[i], modelObj);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Building model failed.", e);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Building model failed.", e);
			}
		}
		// ���ʂ̃R���N�V������ԋp
		return result;
	}
	
	/**
	 * �w�肳�ꂽ�R���|�[�l���g�����t�B�[���h�̒��ŁC���f���I�u�W�F�N�g�ƂȂ�t�B�[���h�̈ꗗ��Ԃ��܂��B
	 * @param target �����ΏۂƂȂ�R���|�[�l���g�I�u�W�F�N�g
	 * @return ���f���I�u�W�F�N�g�ƂȂ�t�B�[���h�Ɣ��f���ꂽ�t�B�[���h�̔z��
	 */
	private Field[] getTargetModelFields(Component target) {
		// �R���|�[�l���g�̃N���X�I�u�W�F�N�g���擾
		Class<? extends Object> clazz = target.getClass();
		// ���ʂ��i�[����R���N�V�����𐶐�
		List<Field> resultList = new ArrayList<Field>();
		// ���f���t�B�[���h���̏d��������邽�߂̃R���N�V�����𐶐�
		Set<String> modelFieldNameSet = new HashSet<String>();
		// �����Ώۂ��Ȃ��Ȃ邩�CWicket�񋟃N���X�ɂȂ�܂ŌJ��Ԃ�
		while((clazz != null) && (!(isWicketClass(clazz)))) {
			// ��`����Ă���t�B�[���h���擾
			Field[] fields = clazz.getDeclaredFields();
			// �t�B�[���h���ɏ���
			for (int i = 0; i < fields.length; i++) {
				// �A�N�Z�X�\���`�F�b�N
				if (!fields[i].isAccessible()) {
					// �A�N�Z�X�\�ɂ���
					fields[i].setAccessible(true);
				}
				// �T�|�[�g����Ă���t�B�[���h���`�F�b�N
				if (modelBuilder.isSupported(fields[i])) {
					// �t�B�[���h�����擾
					String fieldName = fields[i].getName();
					// ���łɓ����̃��f���t�B�[���h�����݂��邩�`�F�b�N
					// �i��ۃN���X�̃��f���t�B�[���h��D�悵�C�����̐e�N���X�ɂ���t�B�[���h�̓��f���t�B�[���h�Ƃ��Ȃ��j
					if (!modelFieldNameSet.contains(fieldName)) {
						// ���ʂ̃R���N�V�����ɒǉ�
						resultList.add(fields[i]);
						// �d���`�F�b�N�̂��߂ɃR���N�V�����Ƀt�B�[���h����ǉ�
						modelFieldNameSet.add(fields[i].getName());
					}
				}
			}
			// �X�[�p�[�N���X���擾�����l�̌������s��
			clazz = clazz.getSuperclass();
		}
		// ���ʂ�ԋp
		return resultList.toArray(new Field[0]);
	}
	
//	--- �R���|�[�l���g�r���h�֘A���\�b�h
	
	/**
	 * �w�肳�ꂽ�R���|�[�l���g�����e�R���|�[�l���g�t�B�[���h�ɑ΂��āC�R���|�[�l���g�I�u�W�F�N�g���\�z���ăZ�b�g���܂��B
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @param modelMap ���f���I�u�W�F�N�g�̃R���N�V����
	 */
	private void buildComponent(Component target, Map<Field, Object> modelMap) {
		// �R���|�[�l���g�t�B�[���h�𒊏o
		Field[] targetFields = getTargetComponentFields(target);
		// �t�B�[���h���ɏ���
		for (int i = 0; i < targetFields.length; i++) {
			// �t�B�[���h�̒l��null���`�F�b�N
			try {
				if (targetFields[i].get(target) == null) {
					// �t�B�[���h�l�Ƃ���R���|�[�l���g�I�u�W�F�N�g�̐��������f���r���_�[�Ɉ˗�
					Object model = componentBuilder.build(targetFields[i], target, modelMap);
					// �R���|�[�l���g�t�B�[���h�ɃZ�b�g
					targetFields[i].set(target, model);
				}
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Building component failed.", e);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException("Building component failed.", e);
			}
		}
	}
	
	/**
	 * �w�肳�ꂽ�R���|�[�l���g�����t�B�[���h�̒��ŁC�R���|�[�l���g�I�u�W�F�N�g�ƂȂ�t�B�[���h�̈ꗗ��Ԃ��܂��B
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @return �R���|�[�l���g�I�u�W�F�N�g�ƂȂ�t�B�[���h�Ɣ��f���ꂽ�t�B�[���h�̔z��
	 */
	private Field[] getTargetComponentFields(Component target) {
		// �R���|�[�l���g�̃N���X�I�u�W�F�N�g���擾
		Class<? extends Object> clazz = target.getClass();
		// ���ʂ��i�[����R���N�V�����𐶐�
		Map<String, SortableField> fieldMap = new HashMap<String, SortableField>();
		// �����Ώۂ��Ȃ��Ȃ邩�CWicket�񋟃N���X�ɂȂ�܂ŌJ��Ԃ�
		while((clazz != null) && (!(isWicketClass(clazz)))) {
			// ��`����Ă���t�B�[���h���擾
			Field[] fields = clazz.getDeclaredFields();
			// �t�B�[���h���ɏ���
			for (int i = 0; i < fields.length; i++) {
				// �A�N�Z�X�\���`�F�b�N
				if (!fields[i].isAccessible()) {
					// �A�N�Z�X�\�ɂ���
					fields[i].setAccessible(true);
				}
				// �T�|�[�g����Ă���t�B�[���h���`�F�b�N
				if (componentBuilder.isSupported(fields[i])) {
					// ���ɓ����̃t�B�[���h���ǉ����ꂽ���`�F�b�N
					//�i��ۃN���X�̃R���|�[�l���g�t�B�[���h��D�悵�C�����̐e�N���X�ɂ���t�B�[���h�̓R���|�[�l���g�t�B�[���h�Ƃ��Ȃ��j
					if (!fieldMap.containsKey(fields[i].getName())) {
						// ���ʂ̃R���N�V�����ɒǉ�
						fieldMap.put(fields[i].getName(), new SortableField(fields[i]));
					}
				}
			}
			// �X�[�p�[�N���X���擾�����l�̌������s��
			clazz = clazz.getSuperclass();
		}
		// �\�[�g���s��
		Field[] fields = sortFields(fieldMap);
		// ���ʂ�ԋp
		return fields;
	}
	
	/**
	 * �w�肳�ꂽ�t�B�[���h�̈ꗗ�ɂ��āC�e�q�֌W�ɏ]���ă\�[�g���s���C���̌��ʂ�Ԃ��܂��B
	 * @param sortableFieldMap �t�B�[���h���i�[���ꂽ�R���N�V����
	 * @return �\�[�g���ʂ̃t�B�[���h�̔z��
	 */
	private Field[] sortFields(Map<String, SortableField> sortableFieldMap) {
		List<SortableField> rootList = new LinkedList<SortableField>();
		for (SortableField field : sortableFieldMap.values()) {
			String parentName = field.getParentName();
			if (StringUtils.isEmpty(parentName) || parentName.equals("this")) {
				rootList.add(field);
			} else {
				SortableField parentField = sortableFieldMap.get(parentName);
				parentField.addKid(field);
			}
		}
		List<Field> fieldList = new LinkedList<Field>();
		for (SortableField field : rootList) {
			field.accept(fieldList);
		}
		return fieldList.toArray(new Field[0]);
	}
	
	/**
	 * �\�[�g�̂��߂̋@�\�����t�B�[���h�̃��b�p�[�N���X�ł��B
	 */
	private static class SortableField {
		
		/** �t�B�[���h�I�u�W�F�N�g */
		private Field field;
		
		/** ���̃t�B�[���h�̎q�ƂȂ�t�B�[���h�̃R���N�V���� */
		private List<SortableField> kids = new LinkedList<SortableField>();
		
		/**
		 * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
		 * @param field �t�B�[���h�I�u�W�F�N�g
		 */
		private SortableField(Field field) {
			this.field = field;
		}
		
		/**
		 * ���̃t�B�[���h�̐e�ƂȂ�t�B�[���h�̖��O��Ԃ��܂��B
		 * @return �e�̃t�B�[���h�̖��O
		 */
		private String getParentName() {
			WicketComponent annotation = field.getAnnotation(WicketComponent.class);
			return annotation.parent();
		}
		
		/**
		 * ���̃t�B�[���h�̎q�ƂȂ�t�B�[���h��ǉ����܂��B
		 * @param kid �q�ƂȂ�t�B�[���h
		 */
		private void addKid(SortableField kid) {
			kids.add(kid);
		}
		
		/**
		 * �w�肳�ꂽ�R���N�V�����ɁC���g�����t�B�[���h��ǉ����܂��B
		 * ����ɁC�q�̃t�B�[���h�ɂ��Ă��ċA�I�ɌĂяo���܂��B
		 * @param fieldList �t�B�[���h���i�[����ΏۂƂȂ�R���N�V����
		 */
		private void accept(List<Field> fieldList) {
			fieldList.add(field);
			for (SortableField kid : kids) {
				kid.accept(fieldList);
			}
		}
	}
	
}
