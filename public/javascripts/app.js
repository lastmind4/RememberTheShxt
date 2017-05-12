(function(Vue) {
    new Vue({
        el: 'body',

        data: {
            tasks: [],
            newTask: {}
        },

        created: function() {
            this.$http.get('/api/shits').then(function(res) {
                this.tasks = res.data ? res.data : [];
            });
        },

        methods: {
            createTask: function() {
                if (!$.trim(this.newTask.name)) {
                    this.newTask = {};
                    return;
                };

                var lines = this.newTask.name.split("-");
                if (lines.length > 1 ) {
                    this.newTask.category = lines[0];
                    this.newTask.name = this.newTask.name.slice(lines[0].length + 1, this.newTask.name.length);
                } else {
                    this.newTask.category = "";
                    this.newTask.name = this.newTask.name;
                }
                this.newTask.comment = "";
                Vue.http.options.emulateJSON = true;
                this.$http.post('/api/addShit',this.newTask).success(function(res) {
                    this.$http.get('/api/shits').then(function(res) {
                        this.tasks = res.data ? res.data : [];
                    });
                }).error(function(err) {
                    console.log(err);
                });
            },

            deleteTask: function(index) {
                this.$http.delete('/api/delShit/' + index).success(function(res) {
                    this.$http.get('/api/shits').then(function(res) {
                        this.tasks = res.data ? res.data : [];
                    });
                }).error(function(err) {
                    console.log(err);
                });
            },

            updateTask: function(task, completed) {
                this.$http.post('/api/editShit/' + task.id, task).success(function(res) {
                    this.$http.get('/api/shits').then(function(res) {
                        this.tasks = res.data ? res.data : [];
                    });
                }).error(function(err) {
                    console.log(err);
                });
            }
        }
    });
})(Vue);


