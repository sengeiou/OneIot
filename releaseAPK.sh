echo -e "\033[31m 是否修改版本号? \033[0m"
read VERSION_ANSWER
if [ "$VERSION_ANSWER"x = "YES"x ]; then
	echo -e "\033[31m SDK代码是否为最新? \033[0m"
	read SDK_UPDATE_ANSWER
	if [ "$SDK_UPDATE_ANSWER"x = "YES"x ]; then
		./gradlew clean assembleReleaseChannel
	fi
fi

exit 0
