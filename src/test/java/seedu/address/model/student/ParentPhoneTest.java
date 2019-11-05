package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ParentPhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ParentPhone(null));
    }

    @Test
    public void constructor_invalidParentPhone_throwsIllegalArgumentException() {
        String invalidParentPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new ParentPhone(invalidParentPhone));
    }

    @Test
    public void isValidParentPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> ParentPhone.isValidParentPhone(null));

        // invalid phone numbers
        assertFalse(ParentPhone.isValidParentPhone("")); // empty string
        assertFalse(ParentPhone.isValidParentPhone(" ")); // spaces only
        assertFalse(ParentPhone.isValidParentPhone("91")); // less than 3 numbers
        assertFalse(ParentPhone.isValidParentPhone("phone")); // non-numeric
        assertFalse(ParentPhone.isValidParentPhone("9011p041")); // alphabets within digits
        assertFalse(ParentPhone.isValidParentPhone("9312 1534")); // spaces within digits

        // valid phone numbers
        assertTrue(ParentPhone.isValidParentPhone("91135123"));
        assertTrue(ParentPhone.isValidParentPhone("93121534"));
        assertTrue(ParentPhone.isValidParentPhone("12429384"));
    }
}
