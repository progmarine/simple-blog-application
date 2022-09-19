<template>
    <v-app>
         <v-toolbar app>
            Voices
            <v-spacer></v-spacer>
            <span v-if="profile">{{ profile.name }}&nbsp;</span>
            <v-btn v-if="profile" href="/logout" x-small>
                <v-img
                  max-height="25"
                  max-width="25"
                  src="https://img.icons8.com/ios/50/000000/exit.png"
                ></v-img>
            </v-btn>

        </v-toolbar>
        <v-content>
            <v-container v-if="!profile">Authorize please with
               <a href="/login">Google</a>
            </v-container>
            <v-container v-if="profile">
                <messages-list :messages="messages" />
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    import MessagesList from 'components/messages/MessagesList.vue'
    import { addHandler } from 'util/ws.js'
    export default {
        components: {
            MessagesList
        },
        data () {
            return {
                messages: frontEndData.messages,
                profile: frontEndData.profile
            }
        },
        created() {
            addHandler(data => {
                if (data.objectType === "MESSAGE"){
                    const index = this.messages.findIndex(item => item.id === data.body.id)
                    switch (data.eventType) {
                        case 'CREATE':
                        case 'UPDATE':
                            if (index > -1){
                                this.messages.splice(index, 1, data.body)
                            } else {
                                this.messages.push(data.body)
                            }
                            break;
                        case 'REMOVE':
                            this.messages.splice(index,1)
                            break;
                        default:
                            console.error("Looks like the event type is unknown '${data.eventType}'")

                    }
                } else {
                    console.error("Looks like the object/ event type is unknown '${data.objectType}'")
                }

            })
        }

    }
</script>

<style>

</style>
