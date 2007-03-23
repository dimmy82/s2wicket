package org.seasar.wicket.utils;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import wicket.Component;
import wicket.MarkupContainer;
import wicket.Page;
import wicket.markup.html.WebPage;
import wicket.markup.html.panel.Panel;

/**
 * ������Ƃ���������񋟂��郆�[�e�B���e�B�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
public class Gadget {

	/**
	 * �w�肳�ꂽ�N���X��Wicket�Œ񋟂��ꂽ�N���X���ǂ�����Ԃ��܂��B
	 * @param clazz �N���X�I�u�W�F�N�g
	 * @return WebPage, Page, Panel, MarkupContainer, Component �N���X�������ꍇ�� true
	 */
	public static boolean isWicketClass(Class clazz) {
		return (clazz.equals(WebPage.class))
			|| (clazz.equals(Page.class))
			|| (clazz.equals(Panel.class))
			|| (clazz.equals(MarkupContainer.class))
			|| (clazz.equals(Component.class));
	}
	
	/**
	 * �w�肳�ꂽ���\�b�h��writeReplace()���\�b�h���ǂ�����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @return �Y�����\�b�h�������ꍇ�� true
	 */
	public static boolean isWriteReplace(Method method) {
		return isMethod(method, Object.class, new Class[0], "writeReplace");
	}
	
	/**
	 * �w�肳�ꂽ���\�b�h��equals()���\�b�h���ǂ�����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @return �Y�����\�b�h�������ꍇ�� true
	 */
	public static boolean isEquals(Method method) {
		return isMethod(method, boolean.class, new Class[] {Object.class}, "equals");
	}
	
	/**
	 * �w�肳�ꂽ���\�b�h��hashCode()���\�b�h���ǂ�����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @return �Y�����\�b�h�������ꍇ�� true
	 */
	public static boolean isHashCode(Method method) {
		return isMethod(method, int.class, new Class[0], "hashCode");
	}
	
	/**
	 * �w�肳�ꂽ���\�b�h��toString()���\�b�h���ǂ�����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @return �Y�����\�b�h�������ꍇ�� true
	 */
	public static boolean isToString(Method method) {
		return isMethod(method, String.class, new Class[0], "toString");
	}
	
	/**
	 * �w�肳�ꂽ���\�b�h��finalize()���\�b�h���ǂ�����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @return �Y�����\�b�h�������ꍇ�� true
	 */
	public static boolean isFinalize(Method method) {
		return isMethod(method, void.class, new Class[0], "finalize");
	}
	
	public static boolean isGetMethodInterceptor(Method method) {
		return isMethod(method, MethodInterceptor.class, new Class[0], "getMethodInterceptor");
	}

	/**
	 * �w�肳�ꂽ���\�b�h���C�w�肳�ꂽ�����Ɉ�v���邩�ǂ����Ԃ��܂��B
	 * @param method ���\�b�h�I�u�W�F�N�g
	 * @param type �߂�l�̌^
	 * @param argTypes �����̌^�̔z��
	 * @param name ���\�b�h��
	 * @return ��v�����ꍇ�� true
	 */
	public static boolean isMethod(Method method, Class type, Class[] argTypes, String name) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		if ((method.getReturnType() == type)
				&& (parameterTypes.length == argTypes.length)
				&& (method.getName().equals(name))) {
			for (int i = 0; i < argTypes.length; i++) {
				if (parameterTypes[i] != argTypes[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
