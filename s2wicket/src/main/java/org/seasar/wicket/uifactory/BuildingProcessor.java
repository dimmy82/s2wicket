package org.seasar.wicket.uifactory;

import static org.seasar.wicket.utils.Gadget.isWicketClass;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.lang.StringUtils;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.model.IModel;
import wicket.model.PropertyModel;

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
		Map<String, Object> modelMap = buildModel(target);
		// �R���|�[�l���g�̍\�z
		buildComponent(target, modelMap);
	}
	
//	--- ���f���r���h�֘A���\�b�h
	
	/**
	 * �w�肳�ꂽ�R���|�[�l���g�����e���f���t�B�[���h�ɑ΂��āC���f���I�u�W�F�N�g���\�z���ăZ�b�g���܂��B
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 * @return ���f�����ƃ��f���I�u�W�F�N�g���΂Ŋi�[���ꂽ�R���N�V����
	 */
	private Map<String, Object> buildModel(Component target) {
		// �����������f���I�u�W�F�N�g���i�[����R���N�V�����𐶐�
		Map<String, Object> result = new HashMap<String, Object>();
		// ���f���t�B�[���h�𒊏o
		Field[] targetFields = getTargetModelFields(target);
		// �t�B�[���h���ɏ���
		for (int i = 0; i < targetFields.length; i++) {
			// �t�B�[���h�̒l��null���`�F�b�N
			try {
				if (targetFields[i].get(target) == null) {
					// �t�B�[���h�l�Ƃ��郂�f���I�u�W�F�N�g�̐��������f���r���_�[�Ɉ˗�
					Object model = modelBuilder.build(targetFields[i], target);
					// ���f���t�B�[���h�ɃZ�b�g
					targetFields[i].set(target, model);
					// �R���N�V�����ɒǉ�
					result.put(targetFields[i].getName(), model);
				}
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
	private void buildComponent(Component target, Map<String, Object> modelMap) {
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
		List<Field> resultList = new ArrayList<Field>();
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
					// ���ʂ̃R���N�V�����ɒǉ�
					resultList.add(fields[i]);
				}
			}
			// �X�[�p�[�N���X���擾�����l�̌������s��
			clazz = clazz.getSuperclass();
		}
		// ���ʂ�ԋp
		return resultList.toArray(new Field[0]);
	}
	
	
//	--- ���ؗp���\�b�h
	
	/**
	 * �w�肳�ꂽ�R���|�[�l���g�����t�B�[���h�ɑ΂��āC�C���W�F�N�V�������s���܂��B
	 * @param target �����Ώۂ̃R���|�[�l���g�I�u�W�F�N�g
	 */
	void inject(Component target) {
		// �ΏۃI�u�W�F�N�g�̃N���X�I�u�W�F�N�g���擾
		Class<? extends Object> clazz = target.getClass();
		// �C���W�F�N�V���������Ώۂ̃t�B�[���h���擾
		Field[] targetFields = getTargetFields(clazz);
		try {
			// ���f���̃R���N�V����
			Map<String, Object> modelMap = new HashMap<String, Object>();
			// �t�B�[���h���ɏ���
			for (int i = 0; i < targetFields.length; i++) {
				Field targetField = targetFields[i];
				// �t�B�[���h�ɃA�N�Z�X�ł���悤�ɂ���
				if (!targetField.isAccessible()) {
					targetField.setAccessible(true);
				}
				// WicketModel�A�m�e�[�V�����̃t�B�[���h���`�F�b�N
				if (targetField.isAnnotationPresent(WicketModel.class)) {
					// �ΏۃI�u�W�F�N�g�̃t�B�[���h�l��null���`�F�b�N
					if (targetField.get(target) == null) {
						// FIXME �����͎�������
						String methodName = "create" + StringUtils.capitalize(targetField.getName());
						try {
							// create[ModelName]���\�b�h�����݂����ꍇ
							// create���\�b�h�ɃR���|�[�l���g���������肢����
							Method method = clazz.getMethod(methodName, new Class[0]);
							Object model = method.invoke(target, new Object[0]);
							targetField.set(target, model);
							// ���f���̃R���N�V�����ɒǉ�
							modelMap.put(targetField.getName(), model);
						} catch(NoSuchMethodException e) {
							// ���f���I�u�W�F�N�g�𐶐�
							// �t�B�[���h�̌^���擾
							Class<?> targetFieldClazz = targetField.getType();
							// �C���X�^���X�𐶐�
							Object model = targetFieldClazz.newInstance();
							// �t�B�[���h�ɃZ�b�g
							targetField.set(target, model);
							// ���f���̃R���N�V�����ɒǉ�
							modelMap.put(targetField.getName(), model);
						}
					}
				}
			}
			// �t�B�[���h���ɏ���
			for (int i = 0; i < targetFields.length; i++) {
				Field targetField = targetFields[i];
				// �t�B�[���h�ɃA�N�Z�X�ł���悤�ɂ���
				if (!targetField.isAccessible()) {
					targetField.setAccessible(true);
				}
				// WicketComponent�A�m�e�[�V�����̃t�B�[���h���`�F�b�N
				if (targetField.isAnnotationPresent(WicketComponent.class)) {
					// �ΏۃI�u�W�F�N�g�̃t�B�[���h�l��null���`�F�b�N
					if (targetField.get(target) == null) {
						// FIXME �����͎�������
						// �A�m�e�[�V�������擾
						WicketComponent annotation = targetField.getAnnotation(WicketComponent.class);
						// �e�R���|�[�l���g������
						String parentAttr = annotation.parent();
						MarkupContainer parent;
						if (StringUtils.isEmpty(parentAttr) || parentAttr.equals("this")) {
							parent = (MarkupContainer)target;
						} else {
							Field parentField = clazz.getDeclaredField(parentAttr);
							if (!parentField.isAccessible()) {
								parentField.setAccessible(true);
							}
							parent = (MarkupContainer)(parentField.get(target));
						}
						// ���\�b�h��������
						String methodName = "create" + StringUtils.capitalize(targetField.getName()) + "Component";
						try {
							// create[FieldName]Component()���\�b�h�����݂����ꍇ
							// create���\�b�h�ɃR���|�[�l���g���������肢����
							Method method = clazz.getMethod(methodName, MarkupContainer.class);
							Object component = method.invoke(target, parent);
							targetField.set(target, component);
						} catch(NoSuchMethodException e) {
							// �A�m�e�[�V�����̑������擾
							String modelAttr = annotation.model();
							String propertyAttr = annotation.property();
							String wicketIdAttr = annotation.wicketId();
							String fieldName = targetField.getName();
							if (StringUtils.isEmpty(propertyAttr)) {
								propertyAttr = fieldName;
							}
							if (StringUtils.isEmpty(wicketIdAttr)) {
								wicketIdAttr = fieldName;
							}
							// ���f���I�u�W�F�N�g���擾
							Object model;
							if (StringUtils.isNotEmpty(modelAttr)) {
								Field modelField = clazz.getDeclaredField(modelAttr);
								if (!modelField.isAccessible()) {
									modelField.setAccessible(true);
								}
								model = modelField.get(target);
							} else {
								if (modelMap.size() == 1) {
									model = modelMap.values().iterator().next();
								} else {
									throw new IllegalStateException("Attribute[model] not found. Field name is " + targetField.getName() + ".");
								}
							}
							// �v���p�e�B���f���𐶐�
							PropertyModel propertyModel = new PropertyModel(model, propertyAttr);
							// �t�B�[���h�̌^���擾
							Class<?> targetFieldClazz = targetField.getType();
							// ���������R���|�[�l���g�̕ϐ�
							Component component;
							// ���ۃN���X���`�F�b�N
							if (Modifier.isAbstract(targetFieldClazz.getModifiers())) {
								// ���ۃN���X�Ȃ̂ŁC���I�v���L�V����
								Enhancer enhancer = new Enhancer();
								enhancer.setInterfaces(new Class[] {Serializable.class});
								enhancer.setSuperclass(targetFieldClazz);
								enhancer.setCallback(new WicketComponentMethodInterceptor(target, fieldName));
								component = (Component)enhancer.create(new Class[] {String.class, IModel.class}, new Object[] {wicketIdAttr, propertyModel});
							} else {
								// ��ۃN���X�Ȃ̂ŁC�f���ɃC���X�^���X����
								Constructor<?> constructor = targetFieldClazz.getConstructor(String.class, IModel.class);
								component = (Component)constructor.newInstance(wicketIdAttr, propertyModel);
							}
							// �e�R���e�i�ɓo�^
							parent.add(component);
							// �t�B�[���h�ɃC���X�^���X���Z�b�g
							targetField.set(target, component);
						}
					}
				}
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Field injection failed.", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Field injection failed.", e);
		} catch (SecurityException e) {
			throw new IllegalStateException("Field injection failed.", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Field injection failed.", e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Field injection failed.", e);
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException("Field injection failed.", e);
		} catch (InstantiationException e) {
			throw new IllegalStateException("Field injection failed.", e);
		}
	}
	
	private static class WicketComponentMethodInterceptor implements MethodInterceptor, Serializable {
		private Object target;
		private String fieldName;
		public WicketComponentMethodInterceptor(Object target, String fieldName) {
			super();
			this.target = target;
			this.fieldName = fieldName;
		}
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
			if (Modifier.isAbstract(method.getModifiers())) {
				String methodName = method.getName();
				if (methodName.startsWith("on")) {
					Class<? extends Object> clazz = target.getClass();
					Method targetMethod = clazz.getDeclaredMethod(methodName + StringUtils.capitalize(fieldName));
					targetMethod.invoke(target);
					return null;
				} else {
					throw new UnsupportedOperationException("Method[" + methodName + "()] not supported.");
				}
			} else {
				return proxy.invokeSuper(obj, args);
			}
		}
	}

	/**
	 * �����Ώۂ̃t�B�[���h��Ԃ��܂��B
	 * @param clazz �ΏۃN���X�I�u�W�F�N�g
	 * @param fieldValueProducer �t�B�[���h�l�����I�u�W�F�N�g
	 * @return �����Ώۂ̃t�B�[���h�̔z��
	 */
	private Field[] getTargetFields(Class<? extends Object> clazz) {
		// ���ʂ��i�[����R���N�V�����𐶐�
		List<Field> resultList = new ArrayList<Field>();
		// �����Ώۂ��Ȃ��Ȃ邩�CWicket�񋟃N���X�ɂȂ�܂ŌJ��Ԃ�
		while((clazz != null) && (!(isWicketClass(clazz)))) {
			// ��`����Ă���t�B�[���h���擾
			Field[] fields = clazz.getDeclaredFields();
			// �t�B�[���h���ɏ���
			for (int i = 0; i < fields.length; i++) {
				// �T�|�[�g����Ă���t�B�[���h���`�F�b�N
				if (fields[i].isAnnotationPresent(WicketComponent.class)
						|| fields[i].isAnnotationPresent(WicketModel.class)) {
					// ���ʂ̃R���N�V�����ɒǉ�
					resultList.add(fields[i]);
				}
			}
			// �X�[�p�[�N���X���擾�����l�̌������s��
			clazz = clazz.getSuperclass();
		}
		// ���ʂ�ԋp
		return resultList.toArray(new Field[0]);
	}

}
