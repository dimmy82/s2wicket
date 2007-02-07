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

package org.seasar.wicket.injection;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;

import junit.framework.TestCase;
import net.sf.cglib.proxy.MethodInterceptor;

import org.seasar.framework.container.S2Container;
import org.seasar.wicket.injection.ProxyFactory.MethodInterceptorHolder;

import wicket.util.io.IOUtils;

/**
 * {@link ProxyFactory}�̃e�X�g�P�[�X�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
public class ProxyFactoryTest extends TestCase {

	/**
	 * �C���^�t�F�[�X���瓮�I�v���L�V�𐶐����鏈�����e�X�g���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testCreateInterface() throws Exception {
		ServiceInterface serviceInterface = createMock(ServiceInterface.class);
		serviceInterface.foo();
		S2Container container = createMock(S2Container.class);
		expect(container.getComponent(ServiceInterface.class)).andReturn(serviceInterface);
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		expect(containerLocator.get()).andReturn(container);
		ComponentResolver componentResolver = new ComponentResolver(null, ServiceInterface.class, containerLocator);
		replay(serviceInterface);
		replay(container);
		replay(containerLocator);
		Object result = ProxyFactory.create(ServiceInterface.class, componentResolver);
		assertNotNull(result);
		((ServiceInterface)result).foo();
		verify(containerLocator);
		verify(container);
		verify(serviceInterface);
		MethodInterceptor methodInterceptor = ((ProxyFactory.MethodInterceptorHolder)result).getMethodInterceptor();
		assertEquals("toString", methodInterceptor.toString(), result.toString());
		assertEquals("hashCode", methodInterceptor.hashCode(), result.hashCode());
	}
	
	/**
	 * �N���X���瓮�I�v���L�V�𐶐����鏈�����e�X�g���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testCreateClass() throws Exception {
		ServiceClass serviceClass = createMock(
				ServiceClass.class, new Method[] {ServiceClass.class.getMethod("foo", new Class[0])});
		serviceClass.foo();
		S2Container container = createMock(S2Container.class);
		expect(container.getComponent(ServiceClass.class)).andReturn(serviceClass);
		IS2ContainerLocator containerLocator = createMock(IS2ContainerLocator.class);
		expect(containerLocator.get()).andReturn(container);
		ComponentResolver componentResolver = new ComponentResolver(null, ServiceClass.class, containerLocator);
		replay(serviceClass);
		replay(container);
		replay(containerLocator);
		Object result = ProxyFactory.create(ServiceClass.class, componentResolver);
		assertNotNull(result);
		((ServiceClass)result).foo();
		verify(containerLocator);
		verify(container);
		verify(serviceClass);
		MethodInterceptor methodInterceptor = ((ProxyFactory.MethodInterceptorHolder)result).getMethodInterceptor();
		assertEquals("toString", methodInterceptor.toString(), result.toString());
		assertEquals("hashCode", methodInterceptor.hashCode(), result.hashCode());
	}
	
	/**
	 * ���������v���L�V����U�V���A���C�Y���C����𕜌����Đ��������삷�邩�ǂ������e�X�g���܂��B
	 * @throws Exception ���炩�̗�O�����������Ƃ�
	 */
	public void testProxySerialize() throws Exception {
		ComponentResolver componentResolver = new ComponentResolver(
				"", ServiceInterface.class, new S2ContainerLocatorMock());
		Object before = ProxyFactory.create(ServiceInterface.class, componentResolver);
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(before);
			byte[] bs = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(bs);
			ois = new ObjectInputStream(bais);
			Object after = ois.readObject();
			assertEquals(
					((MethodInterceptorHolder)before).getMethodInterceptor(),
					((MethodInterceptorHolder)after).getMethodInterceptor());
		} finally {
			IOUtils.closeQuietly(oos);
			IOUtils.closeQuietly(ois);
		}
	}
	
	/**
	 * Seasar�R���e�i���P�[�^�̃��b�N�N���X�ł��B
	 */
	private static class S2ContainerLocatorMock implements IS2ContainerLocator, Serializable {
		public S2Container get() {
			return null;
		}
	}

	/**
	 * �e�X�g�p�̃C���^�t�F�[�X�ł��B
	 */
	private static interface ServiceInterface {
		
		public void foo();
		
	}
	
	/**
	 * �e�X�g�p�̃N���X�ł��B
	 */
	static class ServiceClass {
		
		public void foo() {
		};
		
	}
	
}
