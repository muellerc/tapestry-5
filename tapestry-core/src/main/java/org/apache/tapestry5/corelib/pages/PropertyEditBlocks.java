// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.corelib.pages;

import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.corelib.components.*;
import org.apache.tapestry5.internal.BeanValidationContext;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.BeanBlockSource;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;

/**
 * A page that exists to contain blocks used to edit different types of properties. The blocks on this page are
 * contributed into the {@link BeanBlockSource} service configuration.
 *
 * @see BeanBlockContribution
 * @see BeanEditForm
 */
public class PropertyEditBlocks
{
    @Environmental
    private PropertyEditContext context;

    @Environmental
    private BeanValidationContext validationContext;

    public String getValidationId()
    {
        return validationContext.getEditorValidationId() + "-" + context.getPropertyId();
    }

    @Component(
            parameters = {"value=context.propertyValue",
                    "label=prop:context.label",
                    "translate=prop:textFieldTranslator",
                    "validate=prop:textFieldValidator",
                    "validationId=validationId",
                    "clientId=prop:context.propertyId",
                    "annotationProvider=context",
                    "ensureClientIdUnique=true"})
    private TextField textField;

    @Component(
            parameters = {"value=context.propertyValue",
                    "label=prop:context.label",
                    "translate=prop:numberFieldTranslator",
                    "validate=prop:numberFieldValidator",
                    "validationId=validationId",
                    "clientId=prop:context.propertyId",
                    "annotationProvider=context",
                    "ensureClientIdUnique=true"})
    private TextField numberField;


    @Component(
            parameters = {"value=context.propertyValue",
                    "label=prop:context.label",
                    "encoder=valueEncoderForProperty",
                    "validationId=validationId",
                    "model=selectModelForProperty",
                    "validate=prop:selectValidator",
                    "clientId=prop:context.propertyId",
                    "ensureClientIdUnique=true"})
    private Select select;

    @SuppressWarnings("unused")
    @Component(
            parameters = {"value=context.propertyValue",
                    "label=prop:context.label",
                    "validationId=validationId",
                    "clientId=prop:context.propertyId",
                    "ensureClientIdUnique=true"})
    private Checkbox checkboxField;

    @SuppressWarnings("unused")
    @Component(
            parameters = {"value=context.propertyValue",
                    "label=prop:context.label",
                    "clientId=prop:context.propertyid",
                    "validate=prop:dateFieldValidator",
                    "ensureClientIdUnique=true"})
    private DateField dateField;

    @SuppressWarnings("unused")
    @Component(
            parameters = {"value=context.propertyValue",
                    "label=prop:context.label",
                    "clientId=prop:context.propertyid",
                    "validate=prop:calendarFieldValidator",
                    "validationId=validationId",
                    "ensureClientIdUnique=true"})
    private DateField calendarField;

    @Component(
            parameters = {"value=context.propertyValue",
                    "label=prop:context.label",
                    "translate=prop:passwordFieldTranslator",
                    "validate=prop:passwordFieldValidator",
                    "validationId=validationId",
                    "clientId=prop:context.propertyId",
                    "annotationProvider=context",
                    "ensureClientIdUnique=true"})
    private PasswordField passwordField;

    @Component(
            parameters = {"value=context.propertyValue",
                    "label=prop:context.label",
                    "translate=prop:textAreaTranslator",
                    "validationId=validationId",
                    "validate=prop:textAreaValidator",
                    "clientId=prop:context.propertyId",
                    "annotationProvider=context",
                    "ensureClientIdUnique=true"})
    private TextArea textArea;


    @Inject
    private TypeCoercer typeCoercer;

    public PropertyEditContext getContext()
    {
        return context;
    }


    public FieldTranslator getTextFieldTranslator()
    {
        return context.getTranslator(textField);
    }

    public FieldValidator getTextFieldValidator()
    {
        return context.getValidator(textField);
    }

    public FieldTranslator getNumberFieldTranslator()
    {
        return context.getTranslator(numberField);
    }

    public FieldValidator getNumberFieldValidator()
    {
        return context.getValidator(numberField);
    }

    public FieldTranslator getPasswordFieldTranslator()
    {
        return context.getTranslator(passwordField);
    }

    public FieldValidator getPasswordFieldValidator()
    {
        return context.getValidator(passwordField);
    }

    public FieldTranslator getTextAreaTranslator()
    {
        return context.getTranslator(textArea);
    }

    public FieldValidator getTextAreaValidator()
    {
        return context.getValidator(textArea);
    }


    public FieldValidator getDateFieldValidator()
    {
        return context.getValidator(dateField);
    }

    public FieldValidator getCalendarFieldValidator()
    {
        return context.getValidator(calendarField);
    }

    public FieldValidator getSelectValidator()
    {
        return context.getValidator(select);
    }

    /**
     * Provide a value encoder for an enum type.
     */
    @SuppressWarnings("unchecked")
    public ValueEncoder getValueEncoderForProperty()
    {
        return new EnumValueEncoder(typeCoercer, context.getPropertyType());
    }

    /**
     * Provide a select mode for an enum type.
     */
    @SuppressWarnings("unchecked")
    public SelectModel getSelectModelForProperty()
    {
        return new EnumSelectModel(context.getPropertyType(), context.getContainerMessages());
    }
}
