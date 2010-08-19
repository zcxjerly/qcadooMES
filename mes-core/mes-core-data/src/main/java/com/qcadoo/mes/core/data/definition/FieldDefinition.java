package com.qcadoo.mes.core.data.definition;

import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Field defines database field or custom field (according to {@link FieldDefinition#isCustomField()}).
 * 
 * Not editable field can't be changed after entity creation.
 * 
 * Definition of database field can't be modified using RAD.
 * 
 * @apiviz.has com.qcadoo.mes.core.data.definition.FieldType
 * @apiviz.owns com.qcadoo.mes.core.data.definition.FieldValidator
 */
public final class FieldDefinition {

    private final String name;

    private FieldType type;

    private Set<FieldValidator> validators;

    private boolean editable;

    private boolean required;

    private boolean customField;

    private boolean hidden;

    private boolean unique;

    private Object defaultValue;

    public FieldDefinition(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(final FieldType type) {
        this.type = type;
    }

    public Set<FieldValidator> getValidators() {
        return validators;
    }

    public void setValidators(final Set<FieldValidator> validators) {
        this.validators = validators;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(final boolean editable) {
        this.editable = editable;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public boolean isCustomField() {
        return customField;
    }

    public void setCustomField(final boolean customField) {
        this.customField = customField;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(final Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(final boolean unique) {
        this.unique = unique;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 31).append(customField).append(defaultValue).append(editable).append(hidden).append(name)
                .append(required).append(type).append(unique).append(validators).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FieldDefinition)) {
            return false;
        }
        FieldDefinition other = (FieldDefinition) obj;
        return new EqualsBuilder().append(customField, other.customField).append(defaultValue, other.defaultValue)
                .append(editable, other.editable).append(hidden, other.hidden).append(name, other.name)
                .append(required, other.required).append(type, other.type).append(unique, other.unique)
                .append(validators, other.validators).isEquals();
    }

}
