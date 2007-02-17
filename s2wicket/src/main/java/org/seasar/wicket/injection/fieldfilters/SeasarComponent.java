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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Seasar�R���|�[�l���g�̂��߂̃v���[�X�z���_�ƂȂ�t�B�[���h�Ƀ^�O�t�����邽�߂̃A�m�e�[�V�����ł��B
 * <p>���̃A�m�e�[�V�������t�^���ꂽ�t�B�[���h�ɂ́CSeasar�R���e�i�ɂ��Ǘ�����Ă���R���|�[�l���g���C���W�F�N�V��������܂��B
 * �������C���̃A�m�e�[�V�������g�p�\�ɂ���ɂ́C{@link AnnotationFieldFilter}�t�B�[���h�t�B���^��S2Wicket�ɓo�^����Ă���
 * ���Ƃ��K�v�ł��B</p>
 * <p>���̃A�m�e�[�V�����́CWicket���Ǘ�����R���|�[�l���g�N���X�̃t�B�[���h�ɑ΂��ĕt�^���܂��B
 * �Ⴆ�΁CWebPage�N���X�̃T�u�N���X���ňȉ��̂悤�Ɏg�p���܂��B</p>
 * <pre>
 * public class InputPage extends WebPage {
 * 
 *     &#064;SeasarComponent(name="orderService")
 *     private OrderService orderService;
 * 
 *     public InputPage() {
 *         ...
 *         Form form = new Form() {
 *             public void onSubmit() {
 *                 orderService.order(...);
 *             }
 *         }
 *         ...
 *     }
 * }
 * </pre>
 * <p>��L�̃R�[�h�ł́CSeasar�R���e�i��"orderService"�Ƃ������O�œo�^����Ă���R���|�[�l���g���Ăяo���ΏۂƂȂ�܂��B</p>
 * <p>Wicket�ł́C�Ǘ����ɂ���R���|�[�l���g��HTTP�Z�b�V�����Ɋi�[����邽�߁C�i�������\���������Ă��܂��B
 * ���̂��߁C����Seasar�R���|�[�l���g�I�u�W�F�N�g���t�B�[���h�ɑ�����Ă��܂��ƁC�i�����ΏۂƂȂ��Ă��܂��܂��B
 * ������������邽�߂ɁCSeasar�R���|�[�l���g���Ăяo�������������I�v���L�V�I�u�W�F�N�g���t�B�[���h�ɃC���W�F�N�V��������܂��B</p>
 * 
 * @see AnnotationFieldFilter
 * @see FieldFilter
 * @author Yoichiro Tanaka
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SeasarComponent {
	
	/**
	 * �R���|�[�l���g�̖��O�𖾎��I�Ɏw�肷�邽�߂̑����i�C�Ӂj�ł��B
	 * �������ꂪ�w�肳��Ȃ������ꍇ�́C���̃t�B�[���h�̌^�ɂ�郋�b�N�A�b�v���s���܂��B
	 * @return ���O
	 */
	public String name() default "";

}
