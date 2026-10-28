package com.example.spaceandoccupy

import com.example.spaceandoccupy.media.MediaTranscoder
import com.example.spaceandoccupy.model.Property
import com.example.spaceandoccupy.model.User
import com.example.spaceandoccupy.subscription.SubscriptionPolicy
import com.example.spaceandoccupy.subscription.SubscriptionTier
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SubscriptionPolicyTest {

    @Test
    fun freeTrialIsValidForThreeDays() {
        val agent = agent()
        val now = 1_700_000_000_000L
        SubscriptionPolicy.startFreeTrial(agent, now)

        assertTrue(SubscriptionPolicy.isSubscriptionActive(agent, now + SubscriptionPolicy.TRIAL_DURATION_MS))
        assertFalse(SubscriptionPolicy.isSubscriptionActive(agent, now + SubscriptionPolicy.TRIAL_DURATION_MS + 1))
    }

    @Test
    fun paidPlanRestoresAccessAndSetsPremiumPriority() {
        val agent = agent()
        SubscriptionPolicy.activatePaidPlan(agent, SubscriptionTier.PREMIUM, 1000L)

        assertTrue(SubscriptionPolicy.isSubscriptionActive(agent, 1000L + SubscriptionPolicy.PAID_DURATION_MS - 1))
        assertEquals(SubscriptionTier.PREMIUM.name, agent.subscriptionTier)
        assertEquals(10, SubscriptionTier.PREMIUM.feedPriority)
        assertEquals(2500, SubscriptionTier.PREMIUM.monthlyPriceKes)
    }

    @Test
    fun approximateLocationMasksExactPin() {
        val property = Property()
        property.location = "Dagoretti Road, Karen"
        property.concealExactLocation = true
        property.landmarkName = "Karen Shopping Centre"
        property.approximateRadiusMeters = 500

        assertEquals("Within 500m of Karen Shopping Centre", property.publicLocationLabel)
    }

    @Test
    fun transcodingAppendsMobileFormat() {
        assertEquals(
            "https://cdn.example/clip.mp4?fmt=mobile_720p",
            MediaTranscoder.transcodeToMobile("https://cdn.example/clip.mp4")
        )
        assertEquals("H.264 720p mobile", MediaTranscoder.formatLabel())
    }

    private fun agent(): User {
        val user = User()
        user.role = "AGENT"
        return user
    }
}
