/*
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

package org.seasar.wicket.utils;

import ognl.Ognl;
import ognl.OgnlException;
import wicket.Component;

/**
 * OGNL���Ɋւ���ėp�I�ȏ�����񋟂��郆�[�e�B���e�B�N���X�ł��B
 * @author Yoichiro Tanaka
 * @since 1.3.0
 */
public class OgnlUtils {

	/**
	 * �w�肳�ꂽOGNL����]�����C���ʂ�Ԃ��܂��B
	 * @param expression OGNL��
	 * @param target ���[�g�I�u�W�F�N�g�ƂȂ�R���|�[�l���g�I�u�W�F�N�g
	 * @return �]������
	 * @throws OgnlException ���̕]�����ɉ��炩�̗�O�����������Ƃ�
	 */
	public static Object evaluate(String expression, Component target) throws OgnlException {
		// OGNL�����p�[�X
		Object parsedExp = Ognl.parseExpression(expression);
		// OGNL����]��
		Object result = Ognl.getValue(parsedExp, target);
		// ���ʂ�ԋp
		return result;
	}
	
}
