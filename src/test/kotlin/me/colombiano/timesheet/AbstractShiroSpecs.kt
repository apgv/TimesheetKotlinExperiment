package me.colombiano.timesheet

import org.apache.shiro.util.ThreadState
import org.apache.shiro.subject.Subject
import org.apache.shiro.SecurityUtils
import org.apache.shiro.subject.support.SubjectThreadState
import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.util.LifecycleUtils
import org.apache.shiro.UnavailableSecurityManagerException
import org.spek.Spek


//Entire class is a modification of AbstractShiroTest found at http://shiro.apache.org/testing.html
/**
 * Abstract test case enabling Shiro in test environments.
 */
abstract class AbstractShiroSpecs: Spek() {

    private var subjectThreadState: ThreadState? = Any() as? SubjectThreadState
    /**
     * Allows subclasses to set the currently executing {@link Subject} instance.
     *
     * @param subject the Subject instance
     */
    protected fun setSubject(subject: Subject?) {
        clearSubject();
        subjectThreadState = createThreadState(subject);
        subjectThreadState?.bind();
    }

    protected fun getSubject(): Subject? {
        return SecurityUtils.getSubject();
    }

    protected fun createThreadState(subject: Subject?): ThreadState {
        return SubjectThreadState(subject);
    }

    /**
     * Clears Shiro's thread state, ensuring the thread remains clean for future test execution.
     */
    protected fun clearSubject() {
        doClearSubject();
    }

    private fun doClearSubject() {
        if (subjectThreadState != null) {
            subjectThreadState?.clear();
            subjectThreadState = null;
        }
    }

    protected fun setSecurityManager(securityManager: SecurityManager?) {
        return SecurityUtils.setSecurityManager(securityManager);
    }

    protected fun getSecurityManager(): SecurityManager? {
        return SecurityUtils.getSecurityManager();
    }

    fun cleanupSpec() {
        doClearSubject();

        try {
            val securityManager: SecurityManager? = getSecurityManager();
            LifecycleUtils.destroy(securityManager);
        } catch (e: UnavailableSecurityManagerException) {
            //we don't care about this when cleaning up the test environment
            //(for example, maybe the subclass is a unit test and it didn't
            // need a SecurityManager instance because it was using only
            // mock Subject instances)
        }

        setSecurityManager(null);
    }
}
