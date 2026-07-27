import { serve } from "https://deno.land/std@0.168.0/http/server.ts";
import { createClient } from "https://esm.sh/@supabase/supabase-js@2.7.1";
import { google } from "npm:googleapis";

// Get this from Google Cloud Console Service Accounts
const serviceAccount = JSON.parse(Deno.env.get("FIREBASE_SERVICE_ACCOUNT") || "{}");

async function getAccessToken() {
  const jwtClient = new google.auth.JWT(
    serviceAccount.client_email,
    null,
    serviceAccount.private_key,
    ["https://www.googleapis.com/auth/firebase.messaging"],
    null
  );
  const tokens = await jwtClient.authorize();
  return tokens.access_token;
}

serve(async (req) => {
  try {
    const payload = await req.json();

    // Check if it's a new order insertion
    if (payload.type === "INSERT" && payload.table === "orders") {
      const order = payload.record;
      
      const supabase = createClient(
        Deno.env.get("SUPABASE_URL") ?? "",
        Deno.env.get("SUPABASE_ANON_KEY") ?? ""
      );

      // Fetch all available delivery men FCM tokens (You need an fcm_token column in users table)
      const { data: riders } = await supabase
        .from("users")
        .select("fcm_token")
        .eq("role", "delivery_man")
        .eq("isAvailable", true)
        .not("fcm_token", "is", null);

      if (!riders || riders.length === 0) {
        return new Response("No available riders", { status: 200 });
      }

      const accessToken = await getAccessToken();
      const projectId = serviceAccount.project_id;

      // Send to all available riders (in a real app, use topics or loop)
      for (const rider of riders) {
        const message = {
          message: {
            token: rider.fcm_token,
            data: {
              type: "new_order",
              orderId: order.id,
              shopName: order.shopName || "New Restaurant Request",
            },
            notification: {
              title: "New Order Available!",
              body: "A new order is available for delivery.",
            },
            android: {
              priority: "high",
            }
          }
        };

        await fetch(`https://fcm.googleapis.com/v1/projects/${projectId}/messages:send`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`,
          },
          body: JSON.stringify(message),
        });
      }

      return new Response(JSON.stringify({ success: true }), { headers: { "Content-Type": "application/json" } });
    }

    return new Response("Not an insert event", { status: 200 });
  } catch (err) {
    return new Response(String(err?.message ?? err), { status: 500 });
  }
});
